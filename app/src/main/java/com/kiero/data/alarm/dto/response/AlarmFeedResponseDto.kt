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
    @SerialName("feedItemId")
    val id: Long? = null,

    @SerialName("eventType")
    val eventType: String,

    @SerialName("occurredAt")
    val occurredAt: String,

    @SerialName("metadata")
    val metadata: JsonObject
)