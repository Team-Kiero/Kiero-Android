package com.kiero.presentation.auth.parent.model

enum class TermsType(
    val description: String
) {
    SERVICE_AGREEMENT("(필수) KIERO 이용약관 동의"),
    PRIVACY_POLICY("(필수) 개인정보 필수 동의"),
    UNKNOWN("(필수) 알 수 없는 약관");

    companion object {
        fun fromServerString(value: String): TermsType {
            return when (value) {
                "SERVICE_TERMS" -> SERVICE_AGREEMENT
                "PRIVACY_POLICY" -> PRIVACY_POLICY
                else -> UNKNOWN
            }
        }
    }
}