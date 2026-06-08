package com.kiero.data.sse.repositoryimpl

import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.auth.UserRole
import com.kiero.data.sse.model.RawSseEvent
import com.kiero.data.sse.model.SseEvent
import com.kiero.data.sse.model.SseEventType
import com.kiero.data.sse.remote.datasource.SseDataSource
import com.kiero.data.sse.remote.dto.response.CouponDataDto
import com.kiero.data.sse.remote.dto.response.DateDataDto
import com.kiero.data.sse.remote.dto.response.FeedDataDto
import com.kiero.data.sse.remote.dto.response.InviteDataDto
import com.kiero.data.sse.remote.dto.response.MissionDataDto
import com.kiero.data.sse.remote.dto.response.ParentScheduleDataDto
import com.kiero.data.sse.remote.dto.response.ParentWithDrawnDataDto
import com.kiero.data.sse.remote.dto.response.ScheduleDataDto
import com.kiero.data.sse.repository.SseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class SseRepositoryImpl @Inject constructor(
    private val sseDataSource: SseDataSource,
    private val userInfoManager: UserInfoManager,
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
        accessToken: String,
        lastEventId: String?
    ): Flow<SseEvent> {
        return sseDataSource.subscribeEvents(accessToken, lastEventId)
            .mapNotNull { rawEvent ->
                parseEvent(rawEvent)
            }
    }

    private suspend fun parseEvent(raw: RawSseEvent): SseEvent? {
        return when (SseEventType.from(raw.type)) {
            SseEventType.CONNECTED -> {
                Timber.d("SSE connected 이벤트")
                SseEvent.Connected
            }

            SseEventType.HEARTBEAT -> {
                Timber.d("heartbeat 수신 (연결 유지 중)")
                null
            }

            // 부모
            SseEventType.INVITE -> {
                try {
                    val data = json.decodeFromString<InviteDataDto>(raw.data)
                    Timber.d("Invite 이벤트 파싱 성공: childId=${data.childId}")
                    SseEvent.Parent.Invite(data, eventId = raw.id)
                } catch (e: Exception) {
                    Timber.e(e, "Invite 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.FEED -> {
                try {
                    val data = json.decodeFromString<FeedDataDto>(raw.data)
                    Timber.d("📢 Feed 이벤트 파싱 성공: ${data.eventType}")
                    SseEvent.Parent.Feed(data, eventId = raw.id)
                } catch (e: Exception) {
                    Timber.e(e, "Feed 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.MISSION -> {
                try {
                    val data = json.decodeFromString<MissionDataDto>(raw.data)
                    val userRole = userInfoManager.getUserRole()
                    if (userRole == UserRole.PARENT) {
                        Timber.d("📋 Parent MissionComplete 이벤트 파싱 성공: ${data.missionName}")
                        SseEvent.Parent.MissionComplete(data, eventId = raw.id)
                    } else {
                        Timber.d("📋 Child Mission 이벤트 파싱 성공: ${data.missionName}")
                        SseEvent.Kid.Mission(data, eventId = raw.id)
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Mission 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.SCHEDULE -> {
                try {
                    val userRole = userInfoManager.getUserRole()
                    if (userRole == UserRole.PARENT) {
                        val data = json.decodeFromString<ParentScheduleDataDto>(raw.data)
                        Timber.d("📅 Parent Schedule 이벤트 파싱 성공: ${data.childId}")
                        SseEvent.Parent.Schedule(data, eventId = raw.id)
                    } else {
                        val data = json.decodeFromString<ScheduleDataDto>(raw.data)
                        Timber.d("📅 Schedule 이벤트 파싱 성공: ${data.scheduleName}")
                        SseEvent.Kid.Schedule(data, eventId = raw.id)
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Schedule 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.COUPON -> {
                try {
                    val data = json.decodeFromString<CouponDataDto>(raw.data)
                    Timber.d("🎟️ Coupon 이벤트 파싱 성공: ${data.couponName}")
                    SseEvent.Kid.Coupon(data, eventId = raw.id)
                } catch (e: Exception) {
                    Timber.e(e, "Coupon 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.DATE -> {
                try {
                    val data = json.decodeFromString<DateDataDto>(raw.data)
                    Timber.d("Date 이벤트 파싱 성공: ${data.date}")
                    SseEvent.Kid.Date(data, eventId = raw.id)
                } catch (e: Exception) {
                    Timber.e(e, "Date 파싱 실패: ${raw.data}")
                    null
                }
            }

            SseEventType.PARENT_WITH_DRAWN -> {
                try {
                    val data = json.decodeFromString<ParentWithDrawnDataDto>(raw.data)
                    Timber.d("ParentWithDrawnD 이벤트 파싱 성공: ${data.eventType}")
                    SseEvent.Kid.ParentWithDrawn(data, eventId = raw.id)
                } catch (e: Exception) {
                    Timber.e(e, "ParentWithDrawnD 파싱 실패: ${raw.data}")
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
