package com.kiero.data.auth.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.auth.remote.dto.response.ChildrenResponseDto
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthParentService {
    @POST("api/v1/tokens/logout")
    suspend fun postAuthLogout(): BaseResponse<Unit>

    @GET("api/v1/parents/children")
    suspend fun getChildren(): BaseResponse<List<ChildrenResponseDto>>
}