package com.kiero.presentation.auth.kid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.localstorage.TokenManager
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.demo.repository.DemoRepository
import com.kiero.presentation.auth.kid.model.toModel
import com.kiero.presentation.auth.kid.state.KidSignUpState
import com.kiero.presentation.auth.kid.state.KidSignupSideEffect
import com.kiero.presentation.signup.parent.state.ParentSignUpSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class KidSignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val demoRepository: DemoRepository,
    private val tokenRepository: TokenManager
) : ViewModel() {
    private val _state = MutableStateFlow(KidSignUpState())
    val state: StateFlow<KidSignUpState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<KidSignupSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onSignupClick() {
        Timber.e("onSignupClick")
        viewModelScope.launch {
            val currentState = _state.value.kidSignUpUiModel
            if (currentState.firstName.text.isBlank() ||
                currentState.lastName.text.isBlank() ||
                currentState.inviteCode.text.isBlank()) {

                _sideEffect.emit(KidSignupSideEffect.ShowSnackbar("이름이나 초대 코드를 다시 확인해줘."))
                return@launch
            }

            postAuthKidLogin()
        }
    }

    fun postAuthKidLogin() {
        viewModelScope.launch {
            val currentState = _state.value.kidSignUpUiModel

            authRepository.postAuthKidLogin(
                request = currentState.toModel()
            ).onSuccess { authResponse ->
                Timber.d("postAuthKidLogin Success: ${authResponse.lastName}")

                tokenRepository.saveTokens(
                    accessToken = authResponse.accessToken,
                    refreshToken = authResponse.refreshToken
                )

                var currentAttempt = 0
                val maxRetries = 3
                var isDemoSuccess = false

                while (currentAttempt < maxRetries) {
                    val demoResult = demoRepository.postDemo()

                    if (demoResult.isSuccess) {
                        Timber.d("postDemo 성공")
                        isDemoSuccess = true
                        break
                    } else {
                        currentAttempt++
                        val exception = demoResult.exceptionOrNull()
                        Timber.e("postDemo 실패 ($currentAttempt/$maxRetries). 에러: ${exception?.message}")

                        if (currentAttempt < maxRetries) {
                            delay(1000L)
                        } else {
                            Timber.e("3회 재시도 실패. 최종 종료.")
                            val errorMessage = exception?.toHandleErrorMessage() ?: "알 수 없는 오류"
                            _sideEffect.emit(KidSignupSideEffect.ShowSnackbar(errorMessage))
                        }
                    }
                }
                _sideEffect.emit(KidSignupSideEffect.NavigateToKidOnboarding)
            }.onFailure {
                _sideEffect.emit(KidSignupSideEffect.ShowSnackbar("이름이나 초대코드를 다시 확인해줘"))
            }
        }
    }
}