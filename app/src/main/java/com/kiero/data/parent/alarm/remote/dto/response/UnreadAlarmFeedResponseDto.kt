package com.kiero.data.parent.alarm.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnreadAlarmFeedResponseDto(
    @SerialName("hasUnread")
    val hasUnread: Boolean,
    @SerialName("unreadChildIds")
    val unreadChildIds: List<Long>
)
