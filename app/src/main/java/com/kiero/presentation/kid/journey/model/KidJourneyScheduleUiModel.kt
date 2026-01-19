package com.kiero.presentation.kid.journey.model

import androidx.compose.runtime.Immutable
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Immutable
data class KidJourneyScheduleUiModel(
    val order: Int?,
    val startTime: String?,
    val endTime: String?
) {
    private fun parseTime(time: String?): LocalTime {
        return try {
            LocalTime.parse(time)
        } catch (e: Exception) {
            LocalTime.now()
        }
    }

    val amPmFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("a", Locale.KOREAN)
    val timeNumberFormatter: DateTimeFormatter? = DateTimeFormatter.ofPattern("hh : mm", Locale.KOREAN)

    fun getFormattedStartTime(): String {
        val time = parseTime(startTime)
        return "${time.format(amPmFormatter)} ${time.format(timeNumberFormatter)}"
    }

    fun getFormattedEndTime(): String {
        val time = parseTime(endTime)
        return "${time.format(amPmFormatter)} ${time.format(timeNumberFormatter)}"
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

