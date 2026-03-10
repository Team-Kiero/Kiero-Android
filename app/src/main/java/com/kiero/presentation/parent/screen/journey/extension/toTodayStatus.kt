package com.kiero.presentation.parent.screen.journey.extension

import com.kiero.presentation.parent.screen.journey.model.TodayStatus

fun String.toTodayStatus(isOngoing: Boolean): TodayStatus {
    return when (this) {
        "PENDING" -> if (isOngoing) TodayStatus.CURRENT_COMPLETED else TodayStatus.UPCOMING
        "VERIFIED" -> TodayStatus.CURRENT_COMPLETED
        "COMPLETED" -> TodayStatus.PAST_COMPLETED
        "FAILED", "SKIPPED" -> TodayStatus.PAST_MISSED
        else -> TodayStatus.UPCOMING
    }
}