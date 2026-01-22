package com.kiero.presentation.kid.onboarding.state

import androidx.compose.runtime.Immutable

@Immutable
data class KidOnboardingState(
    val kidName: String
)

sealed interface KidOnboardingSideEffect {
    data object NavigateToKid : KidOnboardingSideEffect
}