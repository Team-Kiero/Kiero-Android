package com.kiero.presentation.splash.state

import androidx.compose.runtime.Immutable
import com.kiero.data.config.model.UpdateState

@Immutable
data class SplashState(
    val updateState: UpdateState = UpdateState.NONE,
)

sealed interface SplashSideEffect {
    data object NavigateToAuth : SplashSideEffect
    data object NavigateToParentHome : SplashSideEffect // 부모 오늘의 현황
    data object NavigateToParentGraph : SplashSideEffect // 카카오 로그인
    data object NavigateToKidHome : SplashSideEffect
    data object NavigateToKidOnboarding : SplashSideEffect
}
