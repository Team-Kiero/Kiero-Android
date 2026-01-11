package com.kiero.data.auth.repositoryimpl

import android.content.Context
import com.kiero.core.common.util.handleError
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.auth.local.datasource.AuthLocalDataSource
import com.kiero.data.auth.remote.datasource.AuthDataSource
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto
import com.kiero.data.auth.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {

    override suspend fun loginWithKakao(context: Context): Result<AuthLoginResponseDto> = suspendRunCatching {
        Timber.d("🚀 카카오 로그인 프로세스 시작")

        // 1. 카카오 토큰 가져오기 (실패 시 여기서 바로 catch 블록으로 이동)
        val kakaoToken = authRemoteDataSource.getKakaoToken(context).getOrThrow()
        Timber.d("✅ 카카오 토큰 획득 성공")

        // 2. 서버 로그인 요청 (실패 시 여기서 바로 catch 블록으로 이동)
        val loginResponse = authRemoteDataSource.postAuthLogin(kakaoToken.accessToken).getOrThrow()
        Timber.i("🎉 서버 로그인 최종 성공")

        loginResponse
    }.onFailure { throwable ->
        // 공통 에러 로그 기록 (Throwable을 넘겨서 상세 정보 출력)
        Timber.e(throwable, "❌ 로그인 과정 중 에러 발생")
    }.mapCatching { response ->
        // 성공 시 데이터 반환 (필요 시 여기서 데이터 가공 가능)
        response
    }.recoverCatching { throwable ->
        // 에러 발생 시 사용자가 정의한 handleError 메시지로 변환하여 새로운 Failure 반환
        throw Exception(handleError(throwable))
    }

    override suspend fun saveAuthTokens(accessToken: String, refreshToken: String) = suspendRunCatching {
        Timber.d("💾 토큰 저장 시작")
        authLocalDataSource.saveAccessToken(accessToken)
        authLocalDataSource.saveRefreshToken(refreshToken)
    }.onSuccess {
        Timber.i("✅ 토큰 저장 완료")
    }.onFailure {
        Timber.e(it, "❌ 토큰 저장 실패")
    }
}