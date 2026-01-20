package com.kiero.data.sse.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.sse.model.RawSseEvent
import com.kiero.data.sse.remote.dto.response.SseTokenResponseDto
import kotlinx.coroutines.flow.Flow

interface SseDataSource {
    suspend fun issueSubscribeToken(): BaseResponse<SseTokenResponseDto>

    suspend fun subscribeEvents(accessToken: String): Flow<RawSseEvent>
}