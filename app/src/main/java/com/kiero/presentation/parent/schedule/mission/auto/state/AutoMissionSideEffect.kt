package com.kiero.presentation.parent.schedule.mission.auto.state

sealed class AutoMissionSideEffect {
    /** 알림 메시지 */
    data class ShowToast(val message: String) : AutoMissionSideEffect()

    /** 화면 이동 */
    object NavigateToMain : AutoMissionSideEffect()  // 저장 성공 시

    /** UI 제어 */
    data class ScrollToPage(val index: Int) : AutoMissionSideEffect()
}