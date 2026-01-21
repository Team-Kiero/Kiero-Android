package com.kiero.data.sse.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.sse.remote.dto.response.SseTokenResponseDto
import retrofit2.http.POST
import retrofit2.http.Query

interface SseService {
    @POST("api/v1/tokens/subscribe-token")
    suspend fun issueSubscribeToken(
        @Query("scope") scope: String = "EVENT_SUBSCRIBE"
    ): BaseResponse<SseTokenResponseDto>
}