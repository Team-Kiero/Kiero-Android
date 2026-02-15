package com.kiero.domain.repository.sse

import com.kiero.domain.entity.sse.SseEvent
import kotlinx.coroutines.flow.Flow

interface SseRepository {
    suspend fun issueSubscribeToken(): Result<String>

    suspend fun subscribeEvents(
        accessToken: String
    ): Flow<SseEvent>
}