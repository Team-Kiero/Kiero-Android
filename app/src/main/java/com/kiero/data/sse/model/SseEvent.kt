package com.kiero.data.sse.model

import com.kiero.data.sse.remote.dto.response.FeedDataDto
import com.kiero.data.sse.remote.dto.response.InviteDataDto
import com.kiero.data.sse.remote.dto.response.MissionDataDto
import com.kiero.data.sse.remote.dto.response.ParentScheduleDataDto
import com.kiero.data.sse.remote.dto.response.ScheduleDataDto

sealed interface SseEvent {
    data object Connected : SseEvent

    // 부모 전용
    sealed interface Parent : SseEvent {
        data class Invite(val data: InviteDataDto) : Parent
        data class Feed(val data: FeedDataDto) : Parent
        data class Schedule(val data: ParentScheduleDataDto) : Parent
    }

    // 자녀 전용
    sealed interface Kid : SseEvent {
        data class Mission(val data: MissionDataDto) : Kid
        data class Schedule(val data: ScheduleDataDto) : Kid
    }
}

    // 자녀 전용 이벤트
    data class Mission(
        val data: MissionDataDto
    ) : SseEvent

    data class Schedule(
        val data: ScheduleDataDto
    ) : SseEvent
}