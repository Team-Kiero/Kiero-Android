package com.kiero.data.terms.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.terms.remote.dto.request.TermsAgreementRequestDto
import com.kiero.data.terms.remote.dto.response.TermsAgreementResponseDto
import com.kiero.data.terms.remote.dto.response.TermsItemResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TermsService {
    /**
     * 필수 약관 동의 여부 조회
     * */
    @GET("api/v1/terms/required/status")
    suspend fun getTermsStatus(): BaseResponse<TermsAgreementResponseDto>

    /**
     * 필수 약관 링크 조회
     */
    @GET("api/v1/terms/required")
    suspend fun getTermsLink(): BaseResponse<List<TermsItemResponseDto>>


    /**
     * 필수 약관 등록 - 시작하기에서 호출하기
     * */
    @POST("api/v1/terms/required")
    suspend fun postTermsStatus(
        @Body request: TermsAgreementRequestDto
    ): Response<Unit>
}