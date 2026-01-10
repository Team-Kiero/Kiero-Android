package com.kiero.data.auth.repository

import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto

interface AuthRepository {
    suspend fun loginWithKakao(): Result<AuthLoginResponseDto>
    suspend fun saveAuthTokens(accessToken: String, refreshToken: String)
}