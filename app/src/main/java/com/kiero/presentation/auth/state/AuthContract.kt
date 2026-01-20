package com.kiero.presentation.auth.state

import androidx.compose.runtime.Immutable
import com.kiero.core.model.UiState


@Immutable
data class AuthState(
    val uiState: UiState<Unit> = UiState.Empty
) {
    val isLoading: Boolean
        get() = uiState is UiState.Loading
}

sealed interface AuthSideEffect {
    data object NavigateUp : AuthSideEffect

    data class NavigateToParentSignUp(
        val parentName: String,
        val parentProfileImage: String,
    ) : AuthSideEffect

    data class ShowSnackbar(val message: String) : AuthSideEffect

    data object NavigateToParentGraph : AuthSideEffect
    data object NavigateToParent : AuthSideEffect
    data object NavigateToKid : AuthSideEffect
}
