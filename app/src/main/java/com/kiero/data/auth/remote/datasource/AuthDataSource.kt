package com.kiero.data.auth.remote.datasource

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kiero.core.network.model.BaseResponse
import com.kiero.data.auth.remote.dto.request.kid.AuthKidRequestDto
import com.kiero.data.auth.remote.dto.response.AuthKidResponseDto
import com.kiero.data.auth.remote.dto.response.AuthLoginResponseDto
import com.kiero.data.auth.remote.dto.response.ChildrenResponseDto


interface AuthDataSource {
    suspend fun getKakaoToken(context: Context): Result<OAuthToken>
    suspend fun postAuthLogin(accessToken: String): Result<AuthLoginResponseDto>

    suspend fun postLogout(): BaseResponse<Unit>

    suspend fun getChildren(): BaseResponse<List<ChildrenResponseDto>>

    suspend fun postAuthKidLogin(
        authKidRequestDto: AuthKidRequestDto
    ): BaseResponse<AuthKidResponseDto>
}