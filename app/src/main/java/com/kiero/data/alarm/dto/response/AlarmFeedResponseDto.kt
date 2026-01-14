package com.kiero.data.alarm.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class AlarmFeedResponseDto(
    @SerialName("childName")
    val childName: String,

    @SerialName("feedItems")
    val feedItems: List<FeedItemDto>,

    @SerialName("nextCursor")
    val nextCursor: String?
)

@Serializable
data class FeedItemDto(
    @SerialName("feedItemId") // ID 추가
    val id: Long? = null,

    @SerialName("eventType")
    val eventType: String,  // SCHEDULE, MISSION, COUPON, COMPLETE

    @SerialName("occurredAt")
    val occurredAt: String,  // "2026-01-10T14:30:00"

    @SerialName("metadata")
    val metadata: JsonObject  // 동적 필드
)