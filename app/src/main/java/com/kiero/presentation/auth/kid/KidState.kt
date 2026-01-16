package com.kiero.presentation.auth.kid

import androidx.compose.runtime.Immutable


@Immutable
data class KidState (
    val isLoading : Boolean = false
)

sealed interface KidSideEffect {
    data class ShowSnackBar(val message: String) : KidSideEffect


}

