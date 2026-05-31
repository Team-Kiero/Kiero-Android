package com.kiero.data.terms.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TermsAgreementRequestDto(
    @SerialName("termsIds")
    val termsIds: List<Long>
)