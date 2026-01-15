package com.kiero.presentation.parent.schedule.mission.auto.state

import java.time.LocalDate

sealed class AutoMissionEvent {
    /** 알림장 입력 및 분석 */
    data class OnNoticeTextChanged(val text: String) : AutoMissionEvent()
    object OnAnalyzeClick : AutoMissionEvent()

    /** 결과 확인 네비게이션 */
    data class OnPageChanged(val index: Int) : AutoMissionEvent()

    /** 개별 미션 수정 */
    data class UpdateMissionName(val name: String) : AutoMissionEvent()
    data class UpdateMissionDate(val date: LocalDate) : AutoMissionEvent()
    data class UpdateMissionReward(val reward: Int) : AutoMissionEvent()

    /** 최종 저장 */
    object OnSaveAllClick : AutoMissionEvent()

    /** 취소 */
    object OnCancelClick : AutoMissionEvent()
}