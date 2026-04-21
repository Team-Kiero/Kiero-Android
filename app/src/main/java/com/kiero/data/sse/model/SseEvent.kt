package com.kiero.data.sse.model

import com.kiero.data.sse.remote.dto.response.CouponDataDto
import com.kiero.data.sse.remote.dto.response.DateDataDto
import com.kiero.data.sse.remote.dto.response.FeedDataDto
import com.kiero.data.sse.remote.dto.response.InviteDataDto
import com.kiero.data.sse.remote.dto.response.MissionDataDto
import com.kiero.data.sse.remote.dto.response.ParentScheduleDataDto
import com.kiero.data.sse.remote.dto.response.ScheduleDataDto

sealed class SseEvent {
    open val eventId : String? = null
    data object Connected : SseEvent()

    // 부모 전용
    sealed class Parent : SseEvent() {
        data class Invite(val data: InviteDataDto, override val eventId: String? = null) : Parent()
        data class Feed(val data: FeedDataDto, override val eventId: String? = null) : Parent()
        data class Schedule(val data: ParentScheduleDataDto, override val eventId: String? = null) : Parent()
    }

    // 자녀 전용
    sealed class Kid : SseEvent() {
        data class Mission(val data: MissionDataDto, override val eventId: String? = null) : Kid()
        data class Schedule(val data: ScheduleDataDto, override val eventId: String? = null) : Kid()
        data class Coupon(val data: CouponDataDto, override val eventId: String? = null) : Kid()
        data class Date(val data: DateDataDto, override val eventId: String? = null) : Kid()
    }
}