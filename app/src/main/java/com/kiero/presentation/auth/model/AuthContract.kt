package com.kiero.presentation.auth.model

import androidx.compose.runtime.Immutable


@Immutable
data class AuthState(
    val isLoading: Boolean = false,
)

sealed interface AuthSideEffect {
    data class ShowSnackBar(val message: String) : AuthSideEffect
    data object NavigateUp : AuthSideEffect
}
