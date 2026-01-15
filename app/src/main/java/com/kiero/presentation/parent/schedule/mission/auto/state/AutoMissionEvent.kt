package com.kiero.presentation.parent.schedule.mission.auto.state

import java.time.LocalDate

sealed class AutoMissionEvent {
    /** [SCH-006] 알림장 입력 및 분석 */
    data class OnNoticeTextChanged(val text: String) : AutoMissionEvent()
    object OnAnalyzeClick : AutoMissionEvent() // 분석 API 호출

    /** [SCH-008] 결과 확인 네비게이션 */
    object OnPrevPage : AutoMissionEvent() // 이전 버튼
    object OnNextPage : AutoMissionEvent() // 다음 버튼
    data class OnPageChanged(val index: Int) : AutoMissionEvent() // 스와이프 제스처

    /** [SCH-008] 개별 미션 수정 (SCH-005와 로직 공유) */
    data class UpdateMissionName(val name: String) : AutoMissionEvent()
    data class UpdateMissionDate(val date: LocalDate) : AutoMissionEvent()
    data class UpdateMissionReward(val reward: Int) : AutoMissionEvent()

    /** 최종 저장 */
    object OnSaveAllClick : AutoMissionEvent() // 일괄 생성 API 호출
}