package com.kiero.data.sse.remote.dto.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// Invite 이벤트 (CHILD_JOINED)
@Serializable
data class InviteDataDto(
    @SerialName("eventType")
    val eventType: String,  // "CHILD_JOINED"

    @SerialName("childId")
    val childId: Long
)

// Feed 이벤트 (MISSION_COMPLETED, SCHEDULE_COMPLETED, FIRE_LIT, COUPON_PURCHASED)
@Serializable
data class FeedDataDto(
    @SerialName("eventType")
    val eventType: String,

    @SerialName("feedItemId")
    val feedItemId: Long? = null,

    @SerialName("childId")
    val childId: Long,

    @SerialName("occurredAt")
    val occurredAt: String,

    @SerialName("metadata")
    val metadata: EventMetadataDto
)

@Serializable
data class EventMetadataDto(
    @SerialName("content")
    val content: String? = null,

    @SerialName("imageUrl")
    val imageUrl: String? = null,

    @SerialName("amount")
    val amount: Int? = null
)