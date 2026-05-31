package com.kiero.data.terms.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TermsItemResponseDto(
    @SerialName("termsId")
    val termsId: Long,
    @SerialName("termsType")
    val termsType: String,
    @SerialName("url")
    val url: String,
)