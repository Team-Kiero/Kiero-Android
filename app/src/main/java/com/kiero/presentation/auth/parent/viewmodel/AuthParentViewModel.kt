package com.kiero.presentation.auth.parent.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.auth.model.AuthSideEffect
import com.kiero.presentation.auth.model.AuthState
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
class AuthParentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun loginWithKakao(context: Context) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        authRepository.loginWithKakao(context)
            .onSuccess {
                _state.update { it.copy(isLoading = false) }
            }.onFailure { throwable ->
                _state.update { it.copy(isLoading = false) }
            }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _sideEffect.emit(AuthSideEffect.NavigateUp)
        }
    }
}