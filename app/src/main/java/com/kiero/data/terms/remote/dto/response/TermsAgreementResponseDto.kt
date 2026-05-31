package com.kiero.data.terms.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TermsAgreementResponseDto(
    @SerialName("isRequiredTermsAllAgreed")
    val isRequiredTermsAllAgreed: Boolean
)