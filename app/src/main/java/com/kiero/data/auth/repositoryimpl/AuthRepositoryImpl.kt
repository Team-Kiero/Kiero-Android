package com.kiero.data.auth.repositoryimpl

import android.content.Context
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.TokenManager
import com.kiero.data.auth.model.AuthKidModel
import com.kiero.data.auth.model.AuthKidResponseModel
import com.kiero.data.auth.model.ChildrenModel
import com.kiero.data.auth.model.toDto
import com.kiero.data.auth.model.toModel
import com.kiero.data.auth.remote.datasource.AuthDataSource
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto
import com.kiero.data.auth.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthDataSource,
    private val tokenManager: TokenManager,
) : AuthRepository {
    override suspend fun loginWithKakao(context: Context): Result<AuthLoginResponseDto> = suspendRunCatching {
        Timber.d("🚀 카카오 로그인 프로세스 시작")

        val kakaoToken = authRemoteDataSource.getKakaoToken(context).getOrThrow()
        Timber.d("✅ 카카오 토큰 획득 성공")

        val loginResponse = authRemoteDataSource.postAuthLogin(kakaoToken.accessToken).getOrThrow()
        Timber.i("🎉 서버 로그인 최종 성공")

        tokenManager.saveTokens(
            accessToken = loginResponse.accessToken,
            refreshToken = loginResponse.refreshToken
        )

        loginResponse
    }

    override suspend fun saveAuthTokens(accessToken: String, refreshToken: String) = suspendRunCatching {
        Timber.d("💾 토큰 저장 시작")
        tokenManager.saveTokens(accessToken, refreshToken)
    }

    override suspend fun postLogout(): Result<Unit> = suspendRunCatching {
        authRemoteDataSource.postLogout()
    }

    override suspend fun getChildren(): Result<List<ChildrenModel>> = suspendRunCatching {
        authRemoteDataSource.getChildren().data!!.map { it.toModel() }
    }

    override suspend fun postAuthKidLogin(request: AuthKidModel): Result<AuthKidResponseModel> = suspendRunCatching {
        val loginResponse = authRemoteDataSource.postAuthKidLogin(
            authKidRequestDto = request.toDto()
        ).data!!.toModel()

        tokenManager.saveTokens(
            accessToken = loginResponse.accessToken,
            refreshToken = loginResponse.refreshToken
        )

        Timber.e("postAuthKidLogin $loginResponse")

        loginResponse
    }
}