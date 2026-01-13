package com.kiero.presentation.auth.model

import androidx.compose.runtime.Immutable


@Immutable
data class AuthState(
    val isLoading: Boolean = false,
)

sealed interface AuthSideEffect {
    data object NavigateUp : AuthSideEffect
}
