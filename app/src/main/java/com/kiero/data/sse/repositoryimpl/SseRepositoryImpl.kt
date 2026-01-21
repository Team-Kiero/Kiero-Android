package com.kiero.data.sse.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.sse.remote.datasource.SseDataSource
import com.kiero.data.sse.model.RawSseEvent
import com.kiero.data.sse.model.SseEvent
import com.kiero.data.sse.model.SseEventType
import com.kiero.data.sse.remote.dto.event.FeedDataDto
import com.kiero.data.sse.remote.dto.event.InviteDataDto
import com.kiero.data.sse.remote.dto.event.MissionDataDto
import com.kiero.data.sse.remote.dto.event.ScheduleDataDto
import com.kiero.data.sse.repository.SseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class SseRepositoryImpl @Inject constructor(
    private val sseDataSource: SseDataSource,
    private val json: Json
) : SseRepository {

    override suspend fun issueSubscribeToken(): Result<String> {
        return try {
            val response = sseDataSource.issueSubscribeToken()

            val accessToken = response.data?.accessToken
            if (accessToken != null) {
                Result.success(accessToken)
            } else {
                Result.failure(Exception("SSE 토큰이 null입니다"))
            }
        } catch (e: Exception) {
            Timber.e(e, "SSE 토큰 발급 실패")
            Result.failure(e)
        }
    }

    override suspend fun subscribeEvents(
        accessToken: String
    ): Flow<SseEvent> {
        return sseDataSource.subscribeEvents(accessToken)
            .mapNotNull { rawEvent ->
                parseEvent(rawEvent)
            }
    }

    private fun parseEvent(raw: RawSseEvent): SseEvent? {
        return when (SseEventType.from(raw.type)) {
            SseEventType.CONNECTED -> {
                Timber.d("SSE connected 이벤트")
                SseEvent.Connected
            }

            SseEventType.HEARTBEAT -> {
                Timber.d("heartbeat 수신 (연결 유지 중)")
                null
            }

            SseEventType.INVITE -> {
                try {
                    val data = json.decodeFromString<InviteDataDto>(raw.data)
                    Timber.d("Invite 이벤트 파싱 성공: childId=${data.childId}")
                    SseEvent.Invite(data)
                } catch (e: Exception) {
                    Timber.e(e, "Invite 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.FEED -> {
                try {
                    val data = json.decodeFromString<FeedDataDto>(raw.data)
                    Timber.d("📢 Feed 이벤트 파싱 성공: ${data.eventType}")
                    SseEvent.Feed(data)
                } catch (e: Exception) {
                    Timber.e(e, "Feed 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.MISSION -> {
                try {
                    val data = json.decodeFromString<MissionDataDto>(raw.data)
                    Timber.d("📋 Mission 이벤트 파싱 성공: ${data.missionName}")
                    SseEvent.Mission(data)
                } catch (e: Exception) {
                    Timber.e(e, "Mission 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.SCHEDULE -> {
                try {
                    val data = json.decodeFromString<ScheduleDataDto>(raw.data)
                    Timber.d("📅 Schedule 이벤트 파싱 성공: ${data.scheduleName}")
                    SseEvent.Schedule(data)
                } catch (e: Exception) {
                    Timber.e(e, "Schedule 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.UNKNOWN -> {
                Timber.w("알 수 없는 SSE 이벤트 타입: ${raw.type}")
                null
            }
        }
    }
}