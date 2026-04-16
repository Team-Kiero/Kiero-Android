package com.kiero.presentation.parent.screen.journey.model

import androidx.compose.ui.graphics.Color
import com.kiero.core.designsystem.theme.KieroColors

enum class TodayStatus(
    val description: String,
) {
    PAST_COMPLETED(
        description = "과거 완료",
    ),
    PAST_MISSED(
        description = "과거 미완료",
    ),
    CURRENT_COMPLETED(
        description = "현재 완료",
    ),
    NEXT_UPCOMING(
        description = "다음 일정" // 현재 진행 중의 바로 다음 일정
    ),
    UPCOMING(
        description = "다음 일정", // 먼 미래 일정 - 회색
    ),
    TODAY_COMPLETED(
        description = "오늘 완료",
    );

    fun getColor(themeColors: KieroColors): Color {
        return when (this) {
            PAST_COMPLETED,UPCOMING,TODAY_COMPLETED -> themeColors.gray800
            PAST_MISSED -> themeColors.point
            CURRENT_COMPLETED, NEXT_UPCOMING -> themeColors.main
        }
    }

    fun getDotColor(themeColors: KieroColors) : Color {
        return when (this) {
            PAST_COMPLETED, PAST_MISSED,TODAY_COMPLETED -> themeColors.schedule1
            CURRENT_COMPLETED, NEXT_UPCOMING -> themeColors.main
            UPCOMING -> themeColors.gray800
        }
    }
}
