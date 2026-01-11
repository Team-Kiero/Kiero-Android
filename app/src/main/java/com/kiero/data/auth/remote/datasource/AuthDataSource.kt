package com.kiero.data.auth.remote.datasource

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto


interface AuthDataSource {
    suspend fun getKakaoToken(context: Context): Result<OAuthToken>
    suspend fun postAuthLogin(accessToken: String): Result<AuthLoginResponseDto>
}