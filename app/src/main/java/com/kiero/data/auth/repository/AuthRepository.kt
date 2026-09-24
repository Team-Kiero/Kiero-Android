package com.kiero.data.auth.repository

import android.content.Context
import com.kiero.data.auth.model.AuthKidModel
import com.kiero.data.auth.model.AuthKidResponseModel
import com.kiero.data.auth.model.AuthLoginModel
import com.kiero.data.auth.model.ChildrenModel

interface AuthRepository {
    suspend fun loginWithKakao(context: Context): Result<AuthLoginModel>
    suspend fun saveAuthTokens(accessToken: String, refreshToken: String): Result<Unit>

    suspend fun postLogout(): Result<Unit>

    suspend fun getChildren(): Result<List<ChildrenModel>>

    suspend fun postAuthKidLogin(
        request : AuthKidModel
    ): Result<AuthKidResponseModel>

    suspend fun postReviewerLogin(
        reviewerPassword: String
    ): Result<AuthLoginModel>
}
