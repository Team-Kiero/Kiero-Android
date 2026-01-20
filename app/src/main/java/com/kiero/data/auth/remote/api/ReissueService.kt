package com.kiero.data.auth.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.auth.remote.dto.response.AuthReissueResponseDto
import retrofit2.http.Header
import retrofit2.http.POST

interface ReissueService {
    // 부모용 - 카카오 로그인 refresh
    @POST("api/v1/tokens/reissue/access-token")
    suspend fun reissueAccessToken(
        @Header("Cookie") refreshToken: String
    ): BaseResponse<AuthReissueResponseDto>

    // 아이용 - 아이는 카카오를 사용하지 않으니까 refreshToken으로 감지
    @POST("api/v1/tokens/reissue/tokens")
    suspend fun reissueToken(
        @Header("Cookie") refreshToken: String
    ): BaseResponse<AuthReissueResponseDto>
}
