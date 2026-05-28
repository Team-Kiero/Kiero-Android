package com.kiero.data.auth.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.auth.remote.dto.request.kid.AuthKidRequestDto
import com.kiero.data.auth.remote.dto.response.AuthKidResponseDto
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    // 부모
    @POST("api/v1/parents/login/kakao/access-token")
    suspend fun postAuthLogin(
        @Query("accessToken") accessToken: String
    ): BaseResponse<AuthLoginResponseDto>

    // 아이
    @POST("api/v1/children/login")
    suspend fun postAuthKidLogin(
        @Body body: AuthKidRequestDto
    ): BaseResponse<AuthKidResponseDto>
}