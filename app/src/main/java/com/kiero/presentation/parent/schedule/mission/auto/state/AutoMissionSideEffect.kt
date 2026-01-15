package com.kiero.presentation.parent.schedule.mission.auto.state

sealed class AutoMissionSideEffect {
    /** 알림 메시지 (15초 타임아웃, 유효성 검사 실패 등) */
    data class ShowToast(val message: String) : AutoMissionSideEffect()

    /** 화면 이동 */
    object NavigateToMain : AutoMissionSideEffect() // 저장 성공 시 SCH-004로
    object BackToInput : AutoMissionSideEffect() // 분석 실패/타임아웃 시 SCH-006으로

    /** UI 제어 */
    data class ScrollToPage(val index: Int) : AutoMissionSideEffect() // 에러 발생 시 해당 페이지로 강제 이동
    object HideKeyboard : AutoMissionSideEffect()
}