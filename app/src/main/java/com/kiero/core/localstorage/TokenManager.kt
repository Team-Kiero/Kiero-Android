package com.kiero.core.localstorage

import com.kiero.core.model.auth.UserRole

interface TokenManager {
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearAllData()
    suspend fun clearTokens()
}