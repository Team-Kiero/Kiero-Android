package com.kiero.presentation.kid.journey.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class KidJourneyHeaderUiModel(
    val kidName: String,
    val currentDate: LocalDate,
    val coinCount: Int,
    val earnedStones: Int,
    val totalScheduleCount: Int
)