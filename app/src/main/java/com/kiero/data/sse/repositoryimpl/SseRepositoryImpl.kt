package com.kiero.data.sse.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.sse.remote.datasource.SseDataSource
import com.kiero.data.sse.remote.dto.response.SseEvent
import com.kiero.data.sse.repository.SseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SseRepositoryImpl @Inject constructor(
    private val sseDataSource: SseDataSource
) : SseRepository {

    override suspend fun issueSubscribeToken(): Result<String> = suspendRunCatching {
        val response = sseDataSource.issueSubscribeToken()
        response.data?.accessToken ?: throw IllegalStateException("토큰 발급 실패")
    }

    override suspend fun subscribeEvents(
        accessToken: String
    ): Flow<SseEvent> {
        return sseDataSource.subscribeEvents(accessToken)
    }
}