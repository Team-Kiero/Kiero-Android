package com.kiero.presentation.auth.parent.model

sealed interface KakaoLoginResult {
    data class HasChildren(val firstChildId: Int) : KakaoLoginResult
    data class NoChildren(
        val parentName: String,
        val parentProfileImage: String
    ) : KakaoLoginResult
}
