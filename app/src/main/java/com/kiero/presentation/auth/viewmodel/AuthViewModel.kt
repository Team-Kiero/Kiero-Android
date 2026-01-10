package com.kiero.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.util.handleError
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.auth.model.AuthContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthContract.State())
    val state: StateFlow<AuthContract.State> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AuthContract.SideEffect>()
    val sideEffect = _sideEffect.asSharedFlow() // 외부 노출용은 읽기 전용으로

    // 카카오 로그인 함수
    fun loginWithKakao() = viewModelScope.launch {
        // 1. 로딩 시작
        _state.update { it.copy(isLoading = true) }

        authRepository.loginWithKakao()
            .onSuccess {
                _state.update { it.copy(isLoading = false) }
                _sideEffect.emit(AuthContract.SideEffect.LoginSuccess)
            }.onFailure { throwable ->
                _state.update { it.copy(isLoading = false) }
                val errorMessage = handleError(throwable)
                showSnackBar(errorMessage)
            }
    }

    private fun showSnackBar(message: String) = viewModelScope.launch {
        _sideEffect.emit(AuthContract.SideEffect.ShowSnackBar(message))
    }

    fun navigateUp() = viewModelScope.launch {
        _sideEffect.emit(AuthContract.SideEffect.NavigateUp)
    }
}
