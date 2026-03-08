package com.kiero.presentation.parent.screen.mission.autoadd.state

sealed interface AutoMissionSideEffect {
    data class ShowToast(val message: String) : AutoMissionSideEffect
    data class ShowToastAndNavigate(val message: String) : AutoMissionSideEffect
    data class ScrollToPage(val index: Int) : AutoMissionSideEffect
}