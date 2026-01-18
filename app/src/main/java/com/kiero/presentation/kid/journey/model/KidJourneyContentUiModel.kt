package com.kiero.presentation.kid.journey.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface KidJourneyContentUiModel {

    /**
     * 금일 일정 없음 (NO_SCHEDULE)
     * - 추가 데이터 불필요
     */
    data object NoSchedule : KidJourneyContentUiModel

    /**
     * 첫 번째 일정 시작 전 (FIRST_SCHEDULE)
     * - 일정 이름만 필요
     */
    data class FirstSchedule(
        val scheduleName: String?,
        val scheduleInfo: KidJourneyScheduleUiModel,
        val isSkippable: Boolean
    ) : KidJourneyContentUiModel

    /**
     * 현재 일정 진행 중 (NOW_SCHEDULE_EXIST)
     * - 일정 이름, 불조각 타입 필요
     */
    data class NowSchedule(
        val scheduleName: String?,
        val stoneType: String?,
        val scheduleInfo: KidJourneyScheduleUiModel,
        val isSkippable: Boolean
    ) : KidJourneyContentUiModel

    /**
     * 다음 일정 존재 (NEXT_SCHEDULE_EXIST)
     * - 일정 이름, 불조각 타입 필요
     */
    data class NextSchedule(
        val scheduleName: String?,
        val stoneType: String?,
        val scheduleInfo: KidJourneyScheduleUiModel,
        val isSkippable: Boolean
    ) : KidJourneyContentUiModel

    /**
     * 불 피우기 전 (FIRE_NOT_LIT)
     * - 아이 이름만 필요 (헤더에 이미 있지만 메시지용)
     */
    data class FireNotLit(
        val kidName: String
    ) : KidJourneyContentUiModel

    /**
     * 불 피우기 완료 (FIRE_LIT)
     * - 추가 데이터 불필요
     */
    data object FireLit : KidJourneyContentUiModel
}