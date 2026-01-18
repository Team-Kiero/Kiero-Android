package com.kiero.presentation.parent.schedule.mission.auto.state

import java.time.LocalDate

sealed class AutoMissionEvent {
    data class OnNoticeTextChanged(val text: String) : AutoMissionEvent()
    data class OnPageChanged(val index: Int) : AutoMissionEvent()
    data class UpdateMissionName(val name: String) : AutoMissionEvent()
    data class UpdateMissionDate(val date: LocalDate) : AutoMissionEvent()
    data class UpdateMissionReward(val reward: Int) : AutoMissionEvent()

    object OnSaveAllClick : AutoMissionEvent()
    object OnCancelClick : AutoMissionEvent()
    object OnAnalyzeClick : AutoMissionEvent()
}