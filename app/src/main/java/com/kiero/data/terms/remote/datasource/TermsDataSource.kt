package com.kiero.data.terms.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.terms.remote.dto.request.TermsAgreementRequestDto
import com.kiero.data.terms.remote.dto.response.TermsAgreementResponseDto
import com.kiero.data.terms.remote.dto.response.TermsItemResponseDto
import retrofit2.Response

interface TermsDataSource {
    suspend fun getTermsStatus(): BaseResponse<TermsAgreementResponseDto>

    suspend fun getTermsLink(): BaseResponse<List<TermsItemResponseDto>>

    suspend fun postTermsStatus(request: TermsAgreementRequestDto): Response<Unit>
}