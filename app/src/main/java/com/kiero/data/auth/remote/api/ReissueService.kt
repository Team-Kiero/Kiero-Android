package com.kiero.data.auth.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.auth.remote.dto.response.AuthReissueResponseDto
import retrofit2.http.Header
import retrofit2.http.POST

interface ReissueService {
    @POST("api/v1/tokens/reissue/tokens")
    suspend fun reissueToken(
        @Header("Cookie") refreshToken: String
    ): BaseResponse<AuthReissueResponseDto>
}
