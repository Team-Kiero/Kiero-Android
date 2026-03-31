package com.kiero.presentation.kid.journey.map.model

import androidx.compose.runtime.Immutable
import com.kiero.core.common.extension.toKoreanTimeString
import com.kiero.data.kid.schedule.model.ScheduleProgressItemModel
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType

@Immutable
data class KidMapItemUiModel(
    val name: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val isOngoing: Boolean = false,
    val isNext: Boolean = false,
    val stoneType: KidJourneyStoneType = KidJourneyStoneType.COURAGE,
    val status: KidMapScheduleStatus = KidMapScheduleStatus.PENDING
)

fun ScheduleProgressItemModel.toUiModel(isFireLitToday: Boolean): KidMapItemUiModel {
    val mappedStatus = KidMapScheduleStatus.from(this.status)

    val validIsOngoing = this.isOngoing && !isFireLitToday &&
            (mappedStatus == KidMapScheduleStatus.PENDING || mappedStatus == KidMapScheduleStatus.VERIFIED)

    return KidMapItemUiModel(
        name = this.name,
        startTime = this.startTime.toKoreanTimeString(),
        endTime = this.endTime.toKoreanTimeString(),
        isOngoing = validIsOngoing,
        stoneType = KidJourneyStoneType.from(this.stoneType),
        status = mappedStatus
    )
}

fun List<ScheduleProgressItemModel>.toUiModelList(isFireLitToday: Boolean): List<KidMapItemUiModel> {
    val mappedList = this.map { it.toUiModel(isFireLitToday) }.toMutableList()

    val hasOngoing = mappedList.any { it.isOngoing }
    val hasVerified = mappedList.any { it.status == KidMapScheduleStatus.VERIFIED }

    if (!hasOngoing && !hasVerified) {
        val pendingIndex = mappedList.indexOfFirst { it.status == KidMapScheduleStatus.PENDING }
        if (pendingIndex != -1) {
            mappedList[pendingIndex] = mappedList[pendingIndex].copy(isNext = true)
        }
    }

    return mappedList
}