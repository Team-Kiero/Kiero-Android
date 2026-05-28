package com.kiero.presentation.parent.screen.mypage.main.model

enum class ParentMenuLinkType {
    PRIVACY_POLICY,
    SERVICE_TERMS,
    OPENSOURCE_LICENSE,
    CUSTOMER_SUPPORT,
    UNKNOWN;

    companion object {
        fun from(value: String): ParentMenuLinkType {
            return entries.find { it.name == value } ?: UNKNOWN
        }
    }
}