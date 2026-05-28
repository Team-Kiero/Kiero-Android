package com.kiero.data.terms.model

import com.kiero.data.terms.remote.dto.response.TermsAgreementResponseDto

data class TermsStatusModel(
    val isRequiredTermsAllAgreed: Boolean
)

fun TermsAgreementResponseDto.toModel() = TermsStatusModel(
    isRequiredTermsAllAgreed = this.isRequiredTermsAllAgreed
)