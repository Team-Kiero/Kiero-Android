package com.kiero.data.auth.remote.api

import com.kiero.core.network.model.DummyBaseResponse
import com.kiero.data.auth.remote.dto.response.DummyResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DummyService {
    @GET("api/users")
    suspend fun getDummyLists(
        @Header("x-api-key") apiKey: String = "reqres-free-v1",
        @Query("page") page: Int = 2,
    ): DummyBaseResponse<List<DummyResponseDto>>
}