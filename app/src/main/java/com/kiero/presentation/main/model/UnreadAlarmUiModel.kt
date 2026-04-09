package com.kiero.presentation.main.model

import androidx.compose.runtime.Immutable
import com.kiero.data.parent.alarm.model.UnreadAlarmFeedModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class UnreadAlarmUiModel(
    val hasUnread: Boolean = false,
    val unreadChildIds: ImmutableList<Long> = persistentListOf()
)
fun UnreadAlarmFeedModel.toUiModel() = UnreadAlarmUiModel(
    hasUnread = this.hasUnread,
    unreadChildIds = this.unreadChildIds.toImmutableList()
)
