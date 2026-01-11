package com.kiero.data.auth.local.datasource


interface AuthLocalDataSource {

    // @param token JWT 액세스 토큰
    suspend fun saveAccessToken(token: String)
    // @param token JWT 리프레시 토큰
    suspend fun saveRefreshToken(token: String)
    // @return 저장된 액세스 토큰, 없으면 null
    suspend fun getAccessToken(): String?
    // @return 저장된 리프레시 토큰, 없으면 null
    suspend fun getRefreshToken(): String?
    // 토큰 삭제.
    suspend fun clearTokens()
}