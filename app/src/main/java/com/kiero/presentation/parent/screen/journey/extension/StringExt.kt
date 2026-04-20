package com.kiero.presentation.parent.screen.journey.extension

import com.kiero.core.common.extension.ALARM_TIME_FORMATTER
import com.kiero.presentation.parent.screen.journey.model.TodayStatus
import java.time.LocalTime

fun String.toTodayStatus(
    startTime: String,
    endTime: String,
    currentTime: LocalTime,
    isNextUpcoming: Boolean = false
): TodayStatus {
    val start = runCatching {
        LocalTime.parse(startTime.take(5), ALARM_TIME_FORMATTER)
    }.getOrNull()

    val end = runCatching {
        LocalTime.parse(endTime.take(5), ALARM_TIME_FORMATTER)
    }.getOrNull()

    return when (this) {
        "PENDING" -> when {
            // 현재 시간이 시작~종료 사이 → 현재 일정
            start != null && end != null && currentTime >= start && currentTime < end ->
                TodayStatus.CURRENT_COMPLETED
            // 현재 시간이 시작 이전이고 바로 다음 일정 → 현재 일정으로 활성화
            start != null && currentTime < start && isNextUpcoming ->
                TodayStatus.NEXT_UPCOMING
            else -> TodayStatus.UPCOMING
        }
        "VERIFIED", "COMPLETED" -> when {
            // 현재 시간이 시작~종료 사이일 때만 현재 일정
            start != null && end != null && currentTime >= start && currentTime < end ->
                TodayStatus.CURRENT_COMPLETED
            else -> TodayStatus.PAST_COMPLETED
        }
        "FAILED", "SKIPPED" -> TodayStatus.PAST_MISSED
        else -> TodayStatus.UPCOMING
    }
}

fun String.toLocalTime(): LocalTime? = runCatching {
    LocalTime.parse(take(5), ALARM_TIME_FORMATTER)
}.getOrNull()
