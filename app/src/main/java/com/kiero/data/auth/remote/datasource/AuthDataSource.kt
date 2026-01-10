package com.kiero.data.auth.remote.datasource

import com.kakao.sdk.auth.model.OAuthToken
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto


interface AuthDataSource {
    suspend fun getKakaoToken(): Result<OAuthToken>
    suspend fun postAuthLogin(accessToken: String): Result<AuthLoginResponseDto>
}