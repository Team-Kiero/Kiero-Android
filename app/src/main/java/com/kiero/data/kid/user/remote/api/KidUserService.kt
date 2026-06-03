package com.kiero.data.kid.user.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.user.remote.dto.response.KidParentWithdrawalStatusResponseDto
import retrofit2.http.GET

interface KidUserService {
    /**
     * 부모 탈퇴여부 조회
     * */
    @GET("api/v1/children/parent-withdrawal-status")
    suspend fun getParentWithdrawalStatus(): BaseResponse<KidParentWithdrawalStatusResponseDto>
}