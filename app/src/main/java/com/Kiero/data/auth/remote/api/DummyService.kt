package com.Kiero.data.auth.remote.api

import com.Kiero.core.network.model.DummyBaseResponse
import com.Kiero.data.auth.remote.dto.response.DummyResponseDto
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