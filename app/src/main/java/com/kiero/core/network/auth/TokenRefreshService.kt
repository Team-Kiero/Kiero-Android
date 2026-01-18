package com.kiero.core.network.auth

import com.kiero.core.model.auth.AccessToken
import com.kiero.core.model.auth.RefreshToken

interface TokenRefreshService {
    suspend fun refresh(refreshToken: String): Result<Pair<AccessToken, RefreshToken>>
}