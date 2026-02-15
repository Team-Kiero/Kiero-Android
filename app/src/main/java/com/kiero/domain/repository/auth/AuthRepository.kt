package com.kiero.domain.repository.auth

import android.content.Context
import com.kiero.domain.entity.auth.AuthKidModel
import com.kiero.domain.entity.auth.AuthKidResponseModel
import com.kiero.domain.entity.auth.ChildrenModel
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto

interface AuthRepository {
    suspend fun loginWithKakao(context: Context): Result<AuthLoginResponseDto>
    suspend fun saveAuthTokens(accessToken: String, refreshToken: String): Result<Unit>

    suspend fun postLogout(): Result<Unit>

    suspend fun getChildren(): Result<List<ChildrenModel>>

    suspend fun postAuthKidLogin(
        request : AuthKidModel
    ): Result<AuthKidResponseModel>
}