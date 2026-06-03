package com.kiero.presentation.kid.myspace.policy.model

enum class KidMenuLinkType {
    PRIVACY_POLICY,
    SERVICE_TERMS,
    OPENSOURCE_LICENSE,
    UNKNOWN;

    companion object {
        fun from(value: String): KidMenuLinkType {
            return entries.find { it.name == value } ?: UNKNOWN
        }
    }
}