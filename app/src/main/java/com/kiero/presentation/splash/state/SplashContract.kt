package com.kiero.presentation.splash.state

sealed interface SplashSideEffect {
    data object NavigateToAuth : SplashSideEffect
    data object NavigateToParentHome : SplashSideEffect
    data object NavigateToParentGraph : SplashSideEffect
    data object NavigateToKidHome : SplashSideEffect
    data object NavigateToKidOnboarding : SplashSideEffect
}
