package com.kiero.data.parent.signup.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.signup.remote.dto.reqeust.ParentSignUpRequestDto
import com.kiero.data.parent.signup.remote.dto.response.ParentSignUpResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ParentSignUpService {
    @POST("api/v1/parents/invite")
    suspend fun postParentSignUp(
        @Body request: ParentSignUpRequestDto
    ): BaseResponse<ParentSignUpResponseDto>
}