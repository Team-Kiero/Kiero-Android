package com.kiero.data.sse.repository

import com.kiero.data.sse.remote.dto.response.SseEvent
import kotlinx.coroutines.flow.Flow

interface SseRepository {
    suspend fun issueSubscribeToken(): Result<String>

    suspend fun subscribeEvents(
        accessToken: String
    ): Flow<SseEvent>
}