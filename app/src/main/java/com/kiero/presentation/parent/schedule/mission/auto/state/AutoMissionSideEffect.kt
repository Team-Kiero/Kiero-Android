package com.kiero.presentation.parent.schedule.mission.auto.state

sealed class AutoMissionSideEffect {
    /** 알림 메시지 */
    data class ShowToast(val message: String) : AutoMissionSideEffect()

    /** UI 제어 */
    data class ScrollToPage(val index: Int) : AutoMissionSideEffect()
}