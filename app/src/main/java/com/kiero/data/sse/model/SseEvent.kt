package com.kiero.data.sse.model

import com.kiero.data.sse.remote.dto.event.FeedDataDto
import com.kiero.data.sse.remote.dto.event.InviteDataDto
import com.kiero.data.sse.remote.dto.event.MissionDataDto
import com.kiero.data.sse.remote.dto.event.ScheduleDataDto


sealed class SseEvent {
    // 공통
    data object Connected : SseEvent()

    // 부모 전용 이벤트
    data class Invite(
        val data: InviteDataDto
    ) : SseEvent()

    data class Feed(
        val data: FeedDataDto
    ) : SseEvent()

    // 자녀 전용 이벤트
    data class Mission(
        val data: MissionDataDto
    ) : SseEvent()

    data class Schedule(
        val data: ScheduleDataDto
    ) : SseEvent()
}