package com.kiero.data.demo.remote.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.POST

interface DemoService {
    @DELETE("api/v1/dummy")
    suspend fun deleteDemo(): Response<Unit>

    @POST("api/v1/dummy")
    suspend fun postDemo(): Response<Unit>
}