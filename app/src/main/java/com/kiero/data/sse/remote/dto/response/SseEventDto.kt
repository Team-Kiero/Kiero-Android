package com.kiero.data.sse.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class SseEvent {
    data object Connected : SseEvent()

    data class Invite(
        val data: InviteDataDto
    ) : SseEvent()

    data class Feed(
        val data: FeedDataDto
    ) : SseEvent()
}

@Serializable
data class InviteDataDto(
    @SerialName("eventType")
    val eventType: String,  // CHILD_JOINED
    @SerialName("childId")
    val childId: Long
)

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