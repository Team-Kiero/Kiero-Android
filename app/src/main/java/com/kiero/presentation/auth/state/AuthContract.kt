package com.kiero.presentation.auth.state

import androidx.compose.runtime.Immutable
import com.kiero.core.model.auth.UserRole

@Immutable
data class AuthState(
    val userRole: UserRole? = null
)

sealed interface AuthSideEffect {
    data object NavigateUp : AuthSideEffect

    data object NavigateToParentSignUp : AuthSideEffect

    data class ShowSnackbar(val message: String) : AuthSideEffect

    data object NavigateToParentGraph : AuthSideEffect
    data object NavigateToParent : AuthSideEffect
    data object NavigateToKid : AuthSideEffect

    data object NavigateToSelection : AuthSideEffect
}
