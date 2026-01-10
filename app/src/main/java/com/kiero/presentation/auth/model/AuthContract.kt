package com.kiero.presentation.auth.model

import androidx.compose.runtime.Immutable


object AuthContract {

    @Immutable
    data class State(
        val isLoading: Boolean = false,
    )

    sealed interface SideEffect {
        data class ShowSnackBar(val message: String) : SideEffect
        data object NavigateUp : SideEffect
        data object LoginSuccess : SideEffect
    }
}