package com.kiero.data.auth.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("api/v1/parents/login/access-token")
    suspend fun postAuthLogin(
        @Query("accessToken") accessToken: String
    ): BaseResponse<AuthLoginResponseDto>
}