package com.kiero.presentation.parent.schedule.mission.auto.state

sealed class AutoMissionSideEffect {

    data class ShowToast(val message: String) : AutoMissionSideEffect()

    data class ScrollToPage(val index: Int) : AutoMissionSideEffect()
}