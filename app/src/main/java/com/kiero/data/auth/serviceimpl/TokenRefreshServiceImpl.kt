package com.kiero.data.auth.serviceimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.model.auth.AccessToken
import com.kiero.core.model.auth.RefreshToken
import com.kiero.core.network.auth.TokenRefreshService
import com.kiero.data.auth.remote.api.ReissueService
import javax.inject.Inject

class TokenRefreshServiceImpl @Inject constructor(
    private val reissueService: ReissueService,
    private val tokenManager: TokenManager
) : TokenRefreshService {
    override suspend fun refresh(refreshToken: String): Result<Pair<AccessToken, RefreshToken>> = suspendRunCatching {
        val response = reissueService.reissueToken("Bearer $refreshToken")
        val data = response.data ?: throw IllegalStateException("서버 응답의 Data가 null입니다.")
        val newAccessToken = data.accessToken

        val refreshToken = tokenManager.getRefreshToken() ?: throw IllegalStateException("서버 응답의 RefreshToken이 null입니다.")

        tokenManager.saveTokens(newAccessToken, refreshToken)
        Pair(AccessToken(newAccessToken), RefreshToken(refreshToken))
    }
}
