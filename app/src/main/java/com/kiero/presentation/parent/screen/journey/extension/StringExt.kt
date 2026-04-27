package com.kiero.presentation.parent.screen.journey.extension

import com.kiero.core.common.extension.ALARM_TIME_FORMATTER
import com.kiero.presentation.parent.screen.journey.model.TodayStatus
import java.time.LocalTime

fun String.toTodayStatus(
    startTime: String,
    endTime: String,
    currentTime: LocalTime,
    isOngoing: Boolean,
    isNextUpcoming: Boolean = false
): TodayStatus {
    val start = startTime.toLocalTime()
    val end = endTime.toLocalTime()

    return when (this) {
        "PENDING" -> when {
            isOngoing -> TodayStatus.CURRENT_COMPLETED
            isNextUpcoming -> TodayStatus.NEXT_UPCOMING
            else -> TodayStatus.UPCOMING
        }
        "VERIFIED", "COMPLETED" -> when {
            isOngoing -> TodayStatus.CURRENT_COMPLETED
            // isOngoing=false여도 아직 종료 시간이 안 됐으면 하이라이팅 유지
            end != null && currentTime < end -> TodayStatus.CURRENT_COMPLETED
            else -> TodayStatus.PAST_COMPLETED
        }
        "FAILED", "SKIPPED" -> TodayStatus.PAST_MISSED
        else -> TodayStatus.UPCOMING
    }
}

fun String.toLocalTime(): LocalTime? = runCatching {
    LocalTime.parse(take(5), ALARM_TIME_FORMATTER)
}.getOrNull()
