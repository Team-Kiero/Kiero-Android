package com.kiero.presentation.kid.journey.model

import androidx.compose.runtime.Immutable
import com.kiero.core.common.extension.toKoreanTimeString
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Immutable
data class KidJourneyScheduleUiModel(
    val order: Int?,
    val startTime: String?,
    val endTime: String?
) {
    fun getFormattedStartTime(): String {
        return startTime?.toKoreanTimeString() ?: ""
    }

    fun getFormattedEndTime(): String {
        return endTime?.toKoreanTimeString() ?: ""
    }

    val orderText: String
        get() = when (order) {
            1 -> "첫 번째"
            2 -> "두 번째"
            3 -> "세 번째"
            4 -> "네 번째"
            5 -> "다섯 번째"
            6 -> "여섯 번째"
            7 -> "일곱 번째"
            8 -> "여덟 번째"
            9 -> "아홉 번째"
            10 -> "열 번째"
            else -> "$order 번째"
        }

    val displayTitle: String
        get() = "$orderText 여정 시간"
}

