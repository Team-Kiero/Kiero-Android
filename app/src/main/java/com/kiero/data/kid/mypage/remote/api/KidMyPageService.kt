package com.kiero.data.kid.mypage.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.mypage.remote.dto.response.KidTermResponseDto
import retrofit2.http.GET

interface KidMyPageService {
    /**
     * 아이 마이페이지 외부 링크 조회
     * */
    @GET("api/v1/terms/children")
    suspend fun getKidTermsExternalLink(): BaseResponse<List<KidTermResponseDto>>
}