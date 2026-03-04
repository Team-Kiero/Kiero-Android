package com.kiero.presentation.auth.parent.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.model.UiState
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.domain.login.HandleKakaoLoginResultUseCase
import com.kiero.presentation.auth.parent.model.KakaoLoginResult
import com.kiero.presentation.auth.parent.state.AuthParentState
import com.kiero.presentation.auth.state.AuthSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
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
                ).onSuccess { kakaoLoginResult ->
                    when (kakaoLoginResult) {
                        is KakaoLoginResult.HasChildren ->
                            _sideEffect.emit(AuthSideEffect.NavigateToParentGraph)
                        is KakaoLoginResult.NoChildren ->
                            _sideEffect.emit(
                                AuthSideEffect.NavigateToParentSignUp(
                                    parentName = kakaoLoginResult.parentName,
                                    parentProfileImage = kakaoLoginResult.parentProfileImage
                                )
                            )
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

    private suspend fun handleError(
        throwable: Throwable? = null,
        message: String = throwable?.toHandleErrorMessage().orEmpty()
    ) {
        _state.update { it.copy(uiState = UiState.Failure(message)) }
        _sideEffect.emit(AuthSideEffect.ShowSnackbar(message = message))
    }
}
