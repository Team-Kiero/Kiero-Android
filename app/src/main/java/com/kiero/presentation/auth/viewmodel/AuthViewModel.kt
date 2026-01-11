package com.kiero.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.util.handleError
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.auth.model.SideEffect
import com.kiero.presentation.auth.model.State
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect = _sideEffect.asSharedFlow() // 외부 노출용은 읽기 전용으로
    // 카카오 로그인 함수
    fun loginWithKakao(context: android.content.Context) = viewModelScope.launch {
        Timber.d("로그인 프로세스 시작")
        // 1. 로딩 시작
        _state.update { it.copy(isLoading = true) }

        authRepository.loginWithKakao(context)
            .onSuccess {
                Timber.i("로그인 최종 성공")

                _state.update { it.copy(isLoading = false) }
                _sideEffect.emit(SideEffect.LoginSuccess)
            }.onFailure { throwable ->
                Timber.e(throwable, "로그인 실패 에러 발생")
                _state.update { it.copy(isLoading = false) }
                val errorMessage = handleError(throwable)
                showSnackBar(errorMessage)
            }
    }

    private fun showSnackBar(message: String) = viewModelScope.launch {
        _sideEffect.emit(SideEffect.ShowSnackBar(message))
    }

    fun navigateUp() = viewModelScope.launch {
        _sideEffect.emit(SideEffect.NavigateUp)
    }
}
