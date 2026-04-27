package com.kiero.presentation.parent.screen.journey.model

import com.kiero.core.common.extension.toShortTime
import com.kiero.data.parent.journey.model.ParentJourneyScheduleModel
import com.kiero.presentation.parent.screen.journey.extension.toLocalTime
import com.kiero.presentation.parent.screen.journey.extension.toTodayStatus
import java.time.LocalTime

data class TodayJourneyUiModel(
    val scheduleDetailId : Long = -1,
    val date: String = "",
    val todayMission: String = "",
    val isAuthenticated: Boolean = false,
    val isOngoing: Boolean = false,
    val todayStatus: TodayStatus = TodayStatus.UPCOMING,
    val scheduleLabel: String = ""
)


fun ParentJourneyScheduleModel.toUiModel(
    currentTime: LocalTime = LocalTime.now(),
    isNextUpcoming: Boolean = false,
    hasOngoingSchedule: Boolean = false
): TodayJourneyUiModel {
    val todayStatus = status.toTodayStatus(
        startTime = startTime,
        endTime = endTime,
        currentTime = currentTime,
        isOngoing = isOngoing,
        isNextUpcoming = isNextUpcoming && !hasOngoingSchedule
    )

    return TodayJourneyUiModel(
        scheduleDetailId = scheduleDetailId,
        date = "${startTime.toShortTime()} - ${endTime.toShortTime()}",
        todayMission = name,
        isAuthenticated = status == "VERIFIED" || status == "COMPLETED",
        isOngoing = isOngoing,
        todayStatus = todayStatus,
        scheduleLabel = when (todayStatus) {
            TodayStatus.NEXT_UPCOMING -> "다음 일정"
            TodayStatus.CURRENT_COMPLETED -> {
                val start = startTime.toLocalTime()
                when {
                    // 조기 완료했지만 원래 '다음 일정' 순서였던 경우 -> "다음 일정" 유지
                    isNextUpcoming && !hasOngoingSchedule -> "다음 일정"
                    // 조기 완료했는데 '다음 일정'조차 아닌 아주 먼 미래의 일정인 경우 -> 라벨 숨김
                    start != null && currentTime < start -> ""
                    // 정상적으로 시작 시간이 지났거나 현재 진행 중인 일정인 경우 -> "현재 일정"
                    else -> "현재 일정"
                }
            }
            else -> ""
        }
    )
}