package com.kiero.data.auth.repository

import android.content.Context
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto

interface AuthRepository {
    suspend fun loginWithKakao(context: Context): Result<AuthLoginResponseDto>
    suspend fun saveAuthTokens(accessToken: String, refreshToken: String): Result<Unit>
}