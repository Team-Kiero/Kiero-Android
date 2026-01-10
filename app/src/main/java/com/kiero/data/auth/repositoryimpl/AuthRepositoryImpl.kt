package com.kiero.data.auth.repositoryimpl

import com.kiero.core.common.util.handleError
import com.kiero.data.auth.local.datasource.AuthLocalDataSource
import com.kiero.data.auth.remote.datasource.AuthDataSource
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto
import com.kiero.data.auth.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {

    override suspend fun loginWithKakao(): Result<AuthLoginResponseDto> {
        // 1. 카카오 OAuth 토큰 받기
        val kakaoTokenResult = authRemoteDataSource.getKakaoToken()

        if (kakaoTokenResult.isFailure) {
            val error = kakaoTokenResult.exceptionOrNull()
            return Result.failure(
                Exception(error?.let { handleError(it) } ?: "카카오 로그인 실패")  // ← 추가!
            )
        }

        // 2. 카카오 accessToken으로 우리 서버 로그인
        val kakaoAccessToken = kakaoTokenResult.getOrNull()?.accessToken
            ?: return Result.failure(Exception("카카오 토큰이 없습니다"))

        val loginResult = authRemoteDataSource.postAuthLogin(kakaoAccessToken)

        // 3. 서버 로그인 실패 시 에러 메시지 변환
        if (loginResult.isFailure) {
            val error = loginResult.exceptionOrNull()
            return Result.failure(
                Exception(error?.let { handleError(it) } ?: "서버 로그인 실패")  // ← 추가!
            )
        }

        return loginResult
    }

    override suspend fun saveAuthTokens(accessToken: String, refreshToken: String) {
        authLocalDataSource.saveAccessToken(accessToken)
        authLocalDataSource.saveRefreshToken(refreshToken)
    }
}