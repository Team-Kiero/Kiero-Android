package com.kiero.data.auth.serviceimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.model.auth.AccessToken
import com.kiero.core.model.auth.RefreshToken
import com.kiero.core.model.auth.UserRole
import com.kiero.core.network.auth.TokenRefreshService
import com.kiero.data.auth.remote.api.ReissueService
import okhttp3.Headers
import timber.log.Timber
import javax.inject.Inject

class TokenRefreshServiceImpl @Inject constructor(
    private val reissueService: ReissueService,
    private val tokenManager: TokenManager
) : TokenRefreshService {
    override suspend fun refresh(refreshToken: String, role: UserRole): Result<Pair<AccessToken, RefreshToken>> = suspendRunCatching {
        val response = when (role) {
            UserRole.PARENT -> reissueService.reissueAccessToken("Bearer $refreshToken")
            UserRole.KID -> reissueService.reissueToken("refreshToken=$refreshToken")
        }

        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Timber.e("Token refresh failed: $errorBody")
            throw IllegalStateException("토큰 갱신 실패: ${response.code()} $errorBody")
        }

        val body = response.body() ?: throw IllegalStateException("서버 응답 Body가 null입니다.")
        val data = body.data ?: throw IllegalStateException("서버 응답 Data가 null입니다.")

        val newAccessToken = data.accessToken

        val newRefreshToken = extractRefreshTokenFromHeaders(response.headers())
            ?: refreshToken

        Timber.e("Token refresh success: Access: $newAccessToken, Refresh: $newRefreshToken")

        tokenManager.saveTokens(newAccessToken, newRefreshToken)

        Pair(AccessToken(newAccessToken), RefreshToken(newRefreshToken))
    }

    private fun extractRefreshTokenFromHeaders(headers: Headers): String? {
        val cookies = headers.values("Set-Cookie")

        val refreshTokenCookie = cookies.find { it.contains("refreshToken=") }

        return refreshTokenCookie?.let { cookie ->
            cookie.split(";")
                .firstOrNull { it.trim().startsWith("refreshToken=") }
                ?.substringAfter("=")
        }
    }
}
