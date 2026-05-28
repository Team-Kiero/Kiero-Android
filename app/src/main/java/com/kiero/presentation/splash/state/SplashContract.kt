package com.kiero.presentation.splash.state

sealed interface SplashSideEffect {
    data object NavigateToAuth : SplashSideEffect
    data object NavigateToParentHome : SplashSideEffect // 부모 오늘의 현황
    data object NavigateToParentGraph : SplashSideEffect // 카카오 로그인
    data object NavigateToKidHome : SplashSideEffect
    data object NavigateToKidOnboarding : SplashSideEffect
}
