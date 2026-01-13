package com.kiero.presentation.kid.journey.state

import androidx.compose.runtime.Immutable
import com.kiero.R
import com.kiero.presentation.kid.journey.model.JourneyScheduleModel

enum class ScheduleStatus {
    NO_SCHEDULE,          // 금일 일정 없음
    FIRST_SCHEDULE,       // 오늘의 첫 번째 일정 시작 전
    NEXT_SCHEDULE_EXIST,  // 현재 일정 완료, 다음 일정 존재
    NOW_SCHEDULE_EXIST,   // 현재 일정 진행 중
    FIRE_NOT_LIT,         // 모든 일정 완료, 불 피우기 전
    FIRE_LIT              // 모든 일정 완료 및 불 피우기 성공
}

@Immutable
data class KidJourneyState(
    val isLoading: Boolean = false,
    val kidName: String = "주완",
    val scheduleStatus: ScheduleStatus = ScheduleStatus.NO_SCHEDULE,
    val currentSchedule: JourneyScheduleModel? = null,
    val totalScheduleCount: Int = 0,
    val earnedStones: Int = 0,
) {
    val goblinMessageRes: Int
        get() = when (scheduleStatus) {
            ScheduleStatus.NO_SCHEDULE -> R.string.journey_no_schedule
            ScheduleStatus.FIRST_SCHEDULE -> R.string.journey_first_schedule
            ScheduleStatus.NOW_SCHEDULE_EXIST -> R.string.journey_now_schedule
            ScheduleStatus.NEXT_SCHEDULE_EXIST -> R.string.journey_next_schedule
            ScheduleStatus.FIRE_NOT_LIT -> R.string.journey_fire_not_lit
            ScheduleStatus.FIRE_LIT -> R.string.journey_fire_lit
        }

    val buttonTextRes: Int?
        get() = when (scheduleStatus) {
            ScheduleStatus.FIRST_SCHEDULE, ScheduleStatus.NEXT_SCHEDULE_EXIST, ScheduleStatus.NOW_SCHEDULE_EXIST -> R.string.journey_btn_auth
            ScheduleStatus.FIRE_NOT_LIT -> R.string.journey_btn_fire
            else -> null
        }
}

sealed interface KidJourneySideEffect {
    data object ShowDialog : KidJourneySideEffect

    data class ShowToast(
        val message: String
    ) : KidJourneySideEffect

    data class ShowSnackbar(
        val message: String,
    ) : KidJourneySideEffect
}
