package com.kiero.data.parent.signup.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.signup.remote.dto.request.ParentSignUpRequestDto
import com.kiero.data.parent.signup.remote.dto.response.LinkageKidResponseDto
import com.kiero.data.parent.signup.remote.dto.response.ParentSignUpResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ParentSignUpService {
    @POST("api/v1/parents/invite")
    suspend fun postParentSignUp(
        @Body request: ParentSignUpRequestDto
    ): BaseResponse<ParentSignUpResponseDto>

    @GET("/api/v1/parents/invite?childLastName={childLastName}&childFirstName={childFirstName}")
    suspend fun getLinkageKid(
        @Path("childLastName") childLastName: String,
        @Path("childFirstName") childFirstName: String
    ): BaseResponse<LinkageKidResponseDto>
}