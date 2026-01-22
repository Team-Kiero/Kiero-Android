package com.kiero.data.auth.remote.datasourceimpl

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.network.model.BaseResponse
import com.kiero.data.auth.remote.api.AuthParentService
import com.kiero.data.auth.remote.api.AuthService
import com.kiero.data.auth.remote.datasource.AuthDataSource
import com.kiero.data.auth.remote.dto.request.kid.AuthKidRequestDto
import com.kiero.data.auth.remote.dto.response.AuthKidResponseDto
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto
import com.kiero.data.auth.remote.dto.response.ChildrenResponseDto
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

class AuthDataSourceImpl @Inject constructor(
    private val authService: AuthService,
    private val authParentService: AuthParentService,
) : AuthDataSource {

    override suspend fun getKakaoToken(context: Context): Result<OAuthToken> =
        suspendCancellableCoroutine { continuation ->
            Timber.d("🚀 카카오 토큰 요청 시작")

            // 카카오계정으로 로그인하는 공통 콜백
            val accountCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                when {
                    error != null -> {
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            Timber.d("⚠️ 사용자 웹 로그인 취소")
                            continuation.resume(Result.failure(error))
                        } else {
                            Timber.e(error, "❌ 카카오 계정 로그인 실패")
                            continuation.resume(Result.failure(error))
                        }
                    }
                    token != null -> {
                        Timber.i("✅ 카카오 계정 로그인 성공")
                        continuation.resume(Result.success(token))
                    }
                }
            }

            // 카카오톡 설치 여부에 따라 분기
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                Timber.d("📱 카카오톡 앱 로그인 시도")
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    when {
                        error != null -> {
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                Timber.w("⚠️ 사용자가 로그인을 취소함")
                                continuation.resume(Result.failure(error))
                            } else {
                                Timber.e(error, "⚠️ 앱 로그인 실패 -> 계정 로그인으로 전환")
                                UserApiClient.instance.loginWithKakaoAccount(context, callback = accountCallback)
                            }
                        }
                        token != null -> {
                            Timber.i("✅ 카카오톡 앱 로그인 성공")
                            continuation.resume(Result.success(token))
                        }
                    }
                }
            } else {
                Timber.d("🌐 카카오톡 미설치: 계정 로그인 시도")
                UserApiClient.instance.loginWithKakaoAccount(context, callback = accountCallback)
            }
        }

    /**
     * 카카오 토큰으로 우리 서버에 로그인
     */
    override suspend fun postAuthLogin(accessToken: String): Result<AuthLoginResponseDto> =
        suspendRunCatching {
            Timber.d("📡 서버 로그인 API 호출")
            val response = authService.postAuthLogin(accessToken)
            response.data ?: throw Exception("응답 데이터가 없습니다: ${response.message}")
        }.onSuccess {
            Timber.i("✅ 서버 로그인 API 응답 성공")
        }.onFailure {
            Timber.e(it, "❌ 서버 로그인 API 호출 실패")
        }

    override suspend fun postLogout(): BaseResponse<Unit> = authParentService.postAuthLogout()

    override suspend fun getChildren(): BaseResponse<List<ChildrenResponseDto>> = authParentService.getChildren()

    override suspend fun postAuthKidLogin(authKidRequestDto: AuthKidRequestDto): BaseResponse<AuthKidResponseDto> =
        authService.postAuthKidLogin(body = authKidRequestDto)

}
