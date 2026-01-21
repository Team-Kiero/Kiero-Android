package com.kiero.core.network.auth

import com.kiero.core.localstorage.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookieInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val refreshToken = runBlocking {
            tokenManager.getRefreshToken()
        }

        val newRequest = chain.request().newBuilder().apply {
            if (refreshToken != null) {
                header("Cookie", "refreshToken=$refreshToken")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}