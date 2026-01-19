package com.kiero.presentation.kid.onboarding.state

sealed interface KidOnboardingSideEffect {
    data object NavigateToKid : KidOnboardingSideEffect
}