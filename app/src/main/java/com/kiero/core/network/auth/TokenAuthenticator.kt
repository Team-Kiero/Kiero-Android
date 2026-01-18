package com.kiero.core.network.auth

import com.kiero.core.localstorage.TokenManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefreshService: TokenRefreshService
) : Authenticator {
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        mutex.withLock {
            val currentToken = tokenManager.getAccessToken()

            // 이미 갱신되었는지 확인
            val reqToken = response.request.header("Authorization")?.substringAfter("Bearer ")
            if (reqToken != currentToken && currentToken != null) {
                Timber.d("토큰 이미 갱신됨. 새 토큰으로 재시도: Bearer $currentToken")
                return@withLock response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            val refreshToken = tokenManager.getRefreshToken() ?: return@withLock null // 로그아웃 필요

            val result = tokenRefreshService.refresh(refreshToken)
            if (result.isSuccess) {
                val (newAccess, newRefresh) = result.getOrThrow()
                tokenManager.saveTokens(newAccess.value, newRefresh.value)

                return@withLock response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccess")
                    .build()
            } else {
                tokenManager.clearTokens()
                return@withLock null
            }
        }
    }
}