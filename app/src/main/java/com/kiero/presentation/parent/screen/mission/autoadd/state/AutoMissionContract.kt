package com.kiero.presentation.parent.screen.mission.autoadd.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.parent.screen.mission.autoadd.model.MissionUiModel
import java.time.LocalDate

@Immutable
data class AutoMissionState(
    val noticeText: String = "",

    val isAnalyzing: Boolean = false,
    val isSaving: Boolean = false,

    val missions: List<MissionUiModel> = emptyList(),
    val currentIndex: Int = 0,

    val selectedDate: LocalDate? = null,
    val hasViewedLastPage: Boolean = false,
) {
    val currentScreen: Screen
        get() = when {
            isAnalyzing -> Screen.LOADING
            missions.isNotEmpty() -> Screen.RESULT
            else -> Screen.INPUT
        }

    val currentMission: MissionUiModel?
        get() = missions.getOrNull(currentIndex)

    val currentReward: Int
        get() = currentMission?.reward ?: 20

    val isAnalyzeEnabled: Boolean
        get() = noticeText.length >= 10

    val isSaveEnabled: Boolean
        get() = hasViewedLastPage && missions.all {
            it.name.isNotBlank() &&
                    !it.dueAt.isBefore(LocalDate.now()) &&
                    it.reward > 0
        }

    enum class Screen {
        INPUT,
        LOADING,
        RESULT
    }
}