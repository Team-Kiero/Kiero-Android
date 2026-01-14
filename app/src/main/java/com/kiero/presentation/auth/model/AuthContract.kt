package com.kiero.presentation.auth.model

import androidx.compose.runtime.Immutable


@Immutable
data class AuthState(
    val isLoading: Boolean = false,
)

sealed interface AuthSideEffect {
    data class ShowSnackBar(val message: String) : AuthSideEffect
    data object NavigateUp : AuthSideEffect
    // TODO : [담당자] 자녀 계정 추가를 위한 온보딩 화면 이동 SideEffect 추가 필요
    // data object NavigateToChildOnboarding : AuthSideEffect
}
