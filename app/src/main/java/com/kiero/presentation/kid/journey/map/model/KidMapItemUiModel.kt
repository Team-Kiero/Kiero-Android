package com.kiero.presentation.kid.journey.map.model

import androidx.compose.runtime.Immutable
import com.kiero.data.kid.schedule.model.ScheduleProgressItemModel
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType

@Immutable
data class KidMapItemUiModel(
    val name: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val isOngoing: Boolean = false,
    val stoneType: KidJourneyStoneType = KidJourneyStoneType.COURAGE,
    val status: KidMapScheduleStatus = KidMapScheduleStatus.PENDING
)

fun ScheduleProgressItemModel.toUiModel() = KidMapItemUiModel(
    name = this.name,
    startTime = this.startTime,
    endTime = this.endTime,
    isOngoing = this.isOngoing,
    stoneType = runCatching { KidJourneyStoneType.valueOf(this.stoneType) }.getOrDefault(
        KidJourneyStoneType.COURAGE
    ),
    status = KidMapScheduleStatus.from(this.status)
)