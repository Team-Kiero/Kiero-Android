package com.kiero.data.auth.remote.datasourceimpl

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kiero.data.auth.remote.api.AuthService
import com.kiero.data.auth.remote.datasource.AuthDataSource
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * AuthDataSource 구현체
 */
class AuthDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authService: AuthService,
) : AuthDataSource {

    override suspend fun getKakaoToken(): Result<OAuthToken> =
        suspendCancellableCoroutine { continuation ->

            // 카카오계정으로 로그인하는 공통 콜백
            val accountCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                when {
                    error != null -> continuation.resume(Result.failure(error))
                    token != null -> continuation.resume(Result.success(token))
                }
            }

            // 카카오톡 설치 여부에 따라 분기
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                // 카카오톡으로 로그인 시도
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    when {
                        error != null -> {
                            // 사용자가 의도적으로 취소한 경우
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                // 로그인 취소로 처리 (카카오계정 로그인 시도 안 함)
                                continuation.resume(Result.failure(error))
                            } else {
                                // 기타 에러 (카카오톡에 연결된 계정 없음 등)
                                // → 카카오계정으로 로그인 시도
                                UserApiClient.instance.loginWithKakaoAccount(
                                    context,
                                    callback = accountCallback
                                )
                            }
                        }
                        token != null -> {
                            // 카카오톡 로그인 성공
                            continuation.resume(Result.success(token))
                        }
                    }
                }
            } else {
                // 카카오톡이 없으면 바로 카카오계정으로 로그인
                UserApiClient.instance.loginWithKakaoAccount(context, callback = accountCallback)
            }
        }

    /**
     * 카카오 토큰으로 우리 서버에 로그인
     */
    override suspend fun postAuthLogin(accessToken: String): Result<AuthLoginResponseDto> {
        return try {
            val response = authService.postAuthLogin(accessToken)

            if (response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("응답 데이터가 없습니다: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}