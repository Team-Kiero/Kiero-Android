package com.kiero.data.terms.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.terms.remote.api.TermsService
import com.kiero.data.terms.remote.datasource.TermsDataSource
import com.kiero.data.terms.remote.dto.request.TermsAgreementRequestDto
import com.kiero.data.terms.remote.dto.response.TermsAgreementResponseDto
import com.kiero.data.terms.remote.dto.response.TermsItemResponseDto
import javax.inject.Inject

class TermsDataSourceImpl @Inject constructor(
    private val termsService: TermsService
) : TermsDataSource {
    override suspend fun getTermsStatus(): BaseResponse<TermsAgreementResponseDto> =
        termsService.getTermsStatus()

    override suspend fun getTermsLink(): BaseResponse<List<TermsItemResponseDto>> =
        termsService.getTermsLink()

    override suspend fun postTermsStatus(request: TermsAgreementRequestDto) =
        termsService.postTermsStatus(request)
}