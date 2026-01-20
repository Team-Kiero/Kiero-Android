package com.kiero.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.model.auth.UserRole
import com.kiero.presentation.auth.state.AuthSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun fetchRole(
        role: UserRole
    ) {
        viewModelScope.launch {
            tokenManager.saveUserRole(
                role = role
            )

            if (role == UserRole.PARENT) {
                _sideEffect.emit(
                    AuthSideEffect.NavigateToParent
                )
            } else {
                _sideEffect.emit(
                    AuthSideEffect.NavigateToKid
                )
            }
        }
    }
}