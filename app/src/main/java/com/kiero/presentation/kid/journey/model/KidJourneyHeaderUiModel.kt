package com.kiero.presentation.kid.journey.model

import androidx.compose.runtime.Immutable
import com.kiero.data.schedule.model.ScheduleTodayModel

@Immutable
data class KidJourneyHeaderUiModel(
    val kidName: String = "주완",
    val currentDate: String = "12월 5일 목요일",
    val coinCount: Int = 0,
    val earnedStones: Int? = 0,
    val totalScheduleCount: Int? = 0
)

fun ScheduleTodayModel.toUiModel() : KidJourneyHeaderUiModel {
    return KidJourneyHeaderUiModel(
        kidName = "주완",
        currentDate = "12월 5일 목요일",
        coinCount = 350,
        earnedStones = earnedStones,
        totalScheduleCount = totalSchedule
    )
}