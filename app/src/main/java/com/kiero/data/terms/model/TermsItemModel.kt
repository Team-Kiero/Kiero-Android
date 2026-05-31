package com.kiero.data.terms.model

import com.kiero.data.terms.remote.dto.response.TermsItemResponseDto

data class TermsItemModel(
    val termsId: Long,
    val termsType: String,
    val url: String,
)

fun TermsItemResponseDto.toModel() = TermsItemModel(
    termsId = this.termsId,
    termsType = this.termsType,
    url = this.url,
)
