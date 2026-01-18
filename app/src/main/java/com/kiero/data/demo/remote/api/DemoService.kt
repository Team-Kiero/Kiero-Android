package com.kiero.data.demo.remote.api

import com.kiero.core.network.model.BaseResponse
import retrofit2.http.DELETE
import retrofit2.http.POST

interface DemoService {
    @DELETE("api/v1/dummy")
    suspend fun deleteDemo(): BaseResponse<Unit>

    @POST("api/v1/dummy")
    suspend fun postDemo(): BaseResponse<Unit>
}