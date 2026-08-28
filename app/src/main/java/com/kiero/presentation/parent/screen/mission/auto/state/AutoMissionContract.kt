package com.kiero.presentation.parent.screen.mission.auto.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.parent.screen.mission.auto.model.MissionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Immutable
data class AutoMissionState(
    val noticeText: String = "",

    val isAnalyzing: Boolean = false,
    val isSaving: Boolean = false,

    val missions: ImmutableList<MissionUiModel> = persistentListOf(),
    val currentIndex: Int = 0,

    val selectedDate: LocalDate? = null,
    val showBottomSheet: Boolean = false,
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

    val displayDate: String
        get() = selectedDate?.format(
            DateTimeFormatter.ofPattern("yyyy.MM.dd.(E)", Locale.KOREA)
        ) ?: "마감일을 선택해주세요"

    enum class Screen {
        INPUT,
        LOADING,
        RESULT
    }
}
