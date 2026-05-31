package com.kiero.data.parent.mypage.parent.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.mypage.parent.remote.dto.response.parent.ParentMyProfileResponseDto
import com.kiero.data.parent.mypage.parent.remote.dto.response.parent.ParentTermResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ParentMyPageService {
    // 부모
    /**
     * 부모 프로필 정보 조회
     * */
    @GET("api/v1/parents/me")
    suspend fun getParentMyProfile(): BaseResponse<ParentMyProfileResponseDto>

    /**
     * 부모 마이페이지 외부링크 조회
     * */
    @GET("api/v1/terms/parent")
    suspend fun getParentTermsExternalLink(): BaseResponse<List<ParentTermResponseDto>>

    /**
     * 부모 탈퇴
     * */
    @POST("api/v1/parents/withdraw")
    suspend fun postWithdrawStatus(): Response<Unit>
}