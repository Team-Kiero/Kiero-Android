package com.kiero.presentation.parent.screen.journey.model

import com.kiero.core.common.extension.toShortTime
import com.kiero.data.parent.journey.model.ParentJourneyScheduleModel
import com.kiero.presentation.parent.screen.journey.extension.toTodayStatus

data class TodayJourneyUiModel(
    val id: Int = 0,
    val date: String = "",
    val todayMission: String = "",
    val isAuthenticated: Boolean = false,
    val authImageUrl: String? = null,
    val todayStatus: TodayStatus = TodayStatus.UPCOMING
)


fun ParentJourneyScheduleModel.toUiModel(id: Int) = TodayJourneyUiModel(
    id = id,
    date = "${startTime.toShortTime()} - ${endTime.toShortTime()}",
    todayMission = name,
    isAuthenticated = status == "VERIFIED",
    authImageUrl = imageUrl,
    todayStatus = status.toTodayStatus(isOngoing)

    // "PENDING""SKIPPED""FAILED""VERIFIED""COMPLETED" => 미래, 스킵되면,
)