package com.kiero.data.parent.alarm.model

import com.kiero.data.parent.alarm.remote.dto.response.UnreadAlarmFeedResponseDto

data class UnreadAlarmFeedModel(
    val hasUnread: Boolean,
    val unreadChildIds: List<Long>
)

fun UnreadAlarmFeedResponseDto.toModel() = UnreadAlarmFeedModel(
    hasUnread = this.hasUnread,
    unreadChildIds = this.unreadChildIds
)
