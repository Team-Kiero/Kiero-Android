package com.kiero.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.auth.UserRole
import com.kiero.presentation.auth.state.AuthSideEffect
import com.kiero.presentation.auth.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userInfoManager: UserInfoManager
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun fetchRole(
        role: UserRole
    ) {
        viewModelScope.launch {
            userInfoManager.saveUserRole(
                role = role
            )

            _state.update {
                it.copy(
                    userRole = role
                )
            }
        }
    }

    fun startRole() {
        viewModelScope.launch {
            val role = _state.value.userRole

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
