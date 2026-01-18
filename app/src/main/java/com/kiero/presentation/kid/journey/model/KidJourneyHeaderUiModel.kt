package com.kiero.presentation.kid.journey.model

import androidx.compose.runtime.Immutable
import com.kiero.data.schedule.model.TodayScheduleModel

@Immutable
data class KidJourneyHeaderUiModel(
    val kidName: String,
    val currentDate: String,
    val coinCount: Int,
    val earnedStones: Int,
    val totalScheduleCount: Int
)

fun TodayScheduleModel.toUiModel() : KidJourneyHeaderUiModel {
    return KidJourneyHeaderUiModel(
        kidName = "주완",
        currentDate = "12월 5일 목요일",
        coinCount = 350,
        earnedStones = earnedStones ?: 0,
        totalScheduleCount = totalSchedule
    )
}