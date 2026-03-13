package com.kiero.presentation.parent.screen.journey.model

import com.kiero.core.common.extension.toShortTime
import com.kiero.data.parent.journey.model.ParentJourneyScheduleModel
import com.kiero.presentation.parent.screen.journey.extension.toTodayStatus

data class TodayJourneyUiModel(
    val scheduleDetailId : Long = -1,
    val date: String = "",
    val todayMission: String = "",
    val isAuthenticated: Boolean = false,
    val isOngoing: Boolean = false,
    val todayStatus: TodayStatus = TodayStatus.UPCOMING
)


// "PENDING""SKIPPED""FAILED""VERIFIED""COMPLETED" => 미래, 스킵되면,
fun ParentJourneyScheduleModel.toUiModel() = TodayJourneyUiModel(
    scheduleDetailId = scheduleDetailId,
    date = "${startTime.toShortTime()} - ${endTime.toShortTime()}",
    todayMission = name,
    isAuthenticated = status == "VERIFIED",
    todayStatus = status.toTodayStatus(isOngoing),
    isOngoing = isOngoing
)
