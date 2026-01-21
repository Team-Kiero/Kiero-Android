package com.kiero.core.network.auth

import com.kiero.core.model.auth.AccessToken
import com.kiero.core.model.auth.RefreshToken
import com.kiero.core.model.auth.UserRole

interface TokenRefreshService {
    suspend fun refresh(refreshToken: String, role: UserRole): Result<Pair<AccessToken, RefreshToken>>
}