package com.kiero.data.sse.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.domain.entity.sse.RawSseEvent
import com.kiero.data.sse.remote.dto.response.SseTokenResponseDto
import kotlinx.coroutines.flow.Flow

interface SseDataSource {
    suspend fun issueSubscribeToken(): BaseResponse<SseTokenResponseDto>

    suspend fun subscribeEvents(accessToken: String): Flow<RawSseEvent>
}