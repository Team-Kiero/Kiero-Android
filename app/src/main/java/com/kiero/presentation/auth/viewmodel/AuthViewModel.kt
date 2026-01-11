package com.kiero.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.util.handleError
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.auth.model.AuthSideEffect
import com.kiero.presentation.auth.model.AuthState
import com.kiero.core.common.extension.toHandleErrorMessage
import com.kiero.core.model.UiState
import com.kiero.data.auth.repository.DummyRepository
import com.kiero.presentation.auth.model.DummySideEffect
import com.kiero.presentation.auth.model.DummyState
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
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow() // 외부 노출용은 읽기 전용으로

    private fun showSnackBar(message: String) = viewModelScope.launch {
        _sideEffect.emit(AuthSideEffect.ShowSnackBar(message))
    }

    fun navigateUp() = viewModelScope.launch {
        _sideEffect.emit(AuthSideEffect.NavigateUp)
    }
}
