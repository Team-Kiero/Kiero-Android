package com.kiero.domain.login.model

sealed interface KakaoLoginResult {
    data object NeedTermsAgreement : KakaoLoginResult // 약관 동의 필요
    data class HasChildren(val firstChildId: Int) : KakaoLoginResult // 자녀 있음
    data object NoChildren : KakaoLoginResult // 자녀 없음 (온보딩 필요)
}