package com.kiero.presentation.main.model

import com.kiero.data.parent.alarm.model.UnreadAlarmFeedModel

data class UnreadAlarmUiModel(
    val hasUnread: Boolean = false,
    val unreadChildIds: List<Long> = emptyList()
)
fun UnreadAlarmFeedModel.toUiModel() = UnreadAlarmUiModel(
    hasUnread = this.hasUnread,
    unreadChildIds = this.unreadChildIds
)