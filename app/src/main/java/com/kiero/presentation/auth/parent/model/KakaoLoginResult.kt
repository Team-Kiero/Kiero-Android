package com.kiero.presentation.auth.parent.model

sealed interface KakaoLoginResult {
    data class HasChildren(val firstChildId: Int) : KakaoLoginResult
    data object NoChildren : KakaoLoginResult
}
