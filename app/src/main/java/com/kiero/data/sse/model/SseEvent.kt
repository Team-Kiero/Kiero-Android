package com.kiero.data.sse.model

import com.kiero.data.sse.remote.dto.response.DateDataDto
import com.kiero.data.sse.remote.dto.response.FeedDataDto
import com.kiero.data.sse.remote.dto.response.InviteDataDto
import com.kiero.data.sse.remote.dto.response.MissionDataDto
import com.kiero.data.sse.remote.dto.response.ScheduleDataDto

sealed interface SseEvent {
    // 공통
    data object Connected : SseEvent

    // 부모 전용 이벤트
    data class Invite(
        val data: InviteDataDto
    ) : SseEvent

    data class Feed(
        val data: FeedDataDto
    ) : SseEvent

    // 자녀 전용 이벤트
    data class Mission(
        val data: MissionDataDto
    ) : SseEvent

    data class Schedule(
        val data: ScheduleDataDto
    ) : SseEvent

    data class Date(
        val data: DateDataDto
    ) : SseEvent
}