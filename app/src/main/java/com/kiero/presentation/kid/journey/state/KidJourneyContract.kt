package com.kiero.presentation.kid.journey.state

sealed interface KidJourneySideEffect {
    data object ShowDialog : KidJourneySideEffect

    data class ShowToast(
        val message: String
    ) : KidJourneySideEffect

    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val onAction: () -> Unit = {}
    ) : KidJourneySideEffect
}
