package com.kiero.presentation.auth.parent.model

import androidx.compose.runtime.Immutable

@Immutable
data class AuthParentConsentsUiModel(
    val isTermsAccepted: Boolean = false,
    val isPrivacyPolicyAccepted: Boolean = false
)
