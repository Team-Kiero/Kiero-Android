package com.kiero.presentation.auth.parent.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.UiState
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.terms.repository.TermsRepository
import com.kiero.domain.login.HandleKakaoLoginResultUseCase
import com.kiero.domain.login.model.KakaoLoginResult
import com.kiero.presentation.auth.parent.model.TermsType
import com.kiero.presentation.auth.parent.model.toUiModel
import com.kiero.presentation.auth.parent.state.AuthParentState
import com.kiero.presentation.auth.state.AuthSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthParentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val termsRepository: TermsRepository,
    private val userInfoManager: UserInfoManager,
    private val handleKakaoLoginResultUseCase: HandleKakaoLoginResultUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthParentState())
    val state: StateFlow<AuthParentState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun loginWithKakao(context: Context) = viewModelScope.launch {
        _state.update { it.copy(uiState = UiState.Loading) }

        authRepository.loginWithKakao(context)
            .onSuccess { result ->
                handleKakaoLoginResultUseCase(
                    name = result.name,
                    image = result.image
                ).onSuccess { domainResult: KakaoLoginResult ->
                    when (domainResult) {
                        is KakaoLoginResult.NeedTermsAgreement -> showTermsAgreement()
                        is KakaoLoginResult.HasChildren -> _sideEffect.emit(AuthSideEffect.NavigateToParentGraph)
                        is KakaoLoginResult.NoChildren -> _sideEffect.emit(AuthSideEffect.NavigateToParentSignUp)
                    }

                }.onFailure { throwable ->
                    Timber.e(throwable)
                    handleError(throwable)
                }
            }
            .onFailure { throwable ->
                Timber.e(throwable)
                if (throwable is ClientError && throwable.reason == ClientErrorCause.Cancelled) {
                    handleError(message = "로그인이 취소되었습니다")
                    return@onFailure
                }
                handleError(throwable)
            }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _sideEffect.emit(AuthSideEffect.NavigateToSelection)
            _state.update { it.copy(uiState = UiState.Empty) }
        }
    }

    /**
     * 이용약관 동의, 개인정보 상태를 토글합니다.
     */
    fun toggleTermsAccepted(termsType: TermsType) {
        _state.update { currentState ->
            val updatedList = currentState.termsList.map { term ->
                if (term.termsType == termsType) {
                    term.copy(isAgreed = !term.isAgreed)
                } else {
                    term
                }
            }
            currentState.copy(termsList = updatedList.toImmutableList())
        }
    }

    /**
     * 현재 둘 다 동의 상태면 모두 해제하고, 하나라도 미동의 상태면 모두 동의합니다.
     * 기획 상에는 없지만 혹시나 사용하게 될 수 있어서 일단 구현
     */
    fun toggleAllConsents() {
        _state.update { currentState ->
            val updatedList = currentState.termsList.map { term ->
                term.copy(isAgreed = !currentState.isAllAgreed)
            }
            currentState.copy(termsList = updatedList.toImmutableList())
        }
    }

    fun showTermsAgreement() {
        viewModelScope.launch {
            val isShowTermsAgreement = _state.value.isShowTermsAgreement

            if (!isShowTermsAgreement && _state.value.termsList.isEmpty()) {
                termsRepository.getTermsLink()
                    .onSuccess { terms ->
                        _state.update { currentState ->
                            currentState.copy(termsList = terms.map { it.toUiModel() }.toImmutableList())
                        }
                    }
                    .onFailure {
                        handleError(it)
                        return@launch
                    }
            }

            _state.update {
                it.copy(
                    isShowTermsAgreement = !isShowTermsAgreement,
                    uiState = UiState.Empty
                )
            }
        }
    }

    /**
     * 약관 동의 완료 처리 및 UserInfoManager에 저장
     */
    fun successTermsAgreement() {
        viewModelScope.launch {
            Timber.e("successTermsAgreement")
            if (_state.value.isAllAgreed) {
                val agreedTermsIds = _state.value.termsList.map { it.termsId }

                userInfoManager.saveTermsInfo(isRequiredTermsAllAgreed = _state.value.isAllAgreed)
                userInfoManager.saveAgreedTermsIds(agreedTermsIds)

                _state.update { it.copy(isShowTermsAgreement = false) }
                _sideEffect.emit(AuthSideEffect.NavigateToParentSignUp)
            } else {
                handleError(message = "필수 약관에 모두 동의해주세요.")
            }
        }
    }

    fun navigateToTerms(termsType: TermsType) {
        viewModelScope.launch {
            val targetTerm = _state.value.termsList.find { it.termsType == termsType }

            if (targetTerm != null && targetTerm.url.isNotBlank()) {
                _sideEffect.emit(AuthSideEffect.OpenWebView(targetTerm.url))
            } else {
                handleError(message = "약관 링크를 찾을 수 없습니다.")
            }
        }
    }



    private suspend fun handleError(
        throwable: Throwable? = null,
        message: String = throwable?.toHandleErrorMessage().orEmpty()
    ) {
        _state.update { it.copy(uiState = UiState.Failure(message)) }
        _sideEffect.emit(AuthSideEffect.ShowSnackbar(message = message))
    }
}
