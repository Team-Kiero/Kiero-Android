package com.kiero.data.terms.model

import com.kiero.data.terms.remote.dto.request.TermsAgreementRequestDto

data class TermsAgreementModel(
    val termsIds: List<Long>,
) {
    fun toDto() = TermsAgreementRequestDto(
        termsIds = this.termsIds,
    )
}
