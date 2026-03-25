package com.kiero.presentation.parent.screen.journey.model

import com.kiero.core.common.extension.toShortTime
import com.kiero.data.parent.journey.model.ParentJourneyScheduleModel
import com.kiero.presentation.parent.screen.journey.extension.toTodayStatus
import java.time.LocalTime

data class TodayJourneyUiModel(
    val scheduleDetailId : Long = -1,
    val date: String = "",
    val todayMission: String = "",
    val isAuthenticated: Boolean = false,
    val isOngoing: Boolean = false,
    val todayStatus: TodayStatus = TodayStatus.UPCOMING,
    val scheduleLabel: String = ""
)


fun ParentJourneyScheduleModel.toUiModel(
    currentTime: LocalTime = LocalTime.now(),
    isNextUpcoming: Boolean = false
) = TodayJourneyUiModel(
    scheduleDetailId = scheduleDetailId,
    date = "${startTime.toShortTime()} - ${endTime.toShortTime()}",
    todayMission = name,
    isAuthenticated = status == "VERIFIED",
    isOngoing = isOngoing,
    todayStatus = status.toTodayStatus(
        startTime = startTime,
        endTime = endTime,
        currentTime = currentTime,
        isNextUpcoming = isNextUpcoming
    ),
    scheduleLabel = when {
        isOngoing -> "현재 일정"
        isNextUpcoming -> "다음 일정"
        else -> ""
    }
)
