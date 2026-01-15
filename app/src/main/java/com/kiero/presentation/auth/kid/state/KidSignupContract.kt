package com.kiero.presentation.auth.kid.state

sealed interface KidSignupSideEffect {
    data object ShowDialog : KidSignupSideEffect

    data class ShowSnackbar(
        val message: String,
    ) : KidSignupSideEffect
}