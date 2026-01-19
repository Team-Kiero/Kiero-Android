package com.kiero.presentation.parent.schedule.mission.auto.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.parent.schedule.mission.auto.model.MissionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

@Immutable
data class AutoMissionContract(
    val noticeText: String = "",

    val isAnalyzing: Boolean = false,

    val missions: ImmutableList<MissionUiModel> = persistentListOf(),
    val currentIndex: Int = 0,

    val isSaving: Boolean = false,

    val shouldNavigateBack: Boolean = false,
) {
    val isAnalyzeEnabled: Boolean = noticeText.trim().length in 10..1000

    val currentMission: MissionUiModel? = missions.getOrNull(currentIndex)

    val isSaveEnabled: Boolean = missions.isNotEmpty() &&
            currentIndex == missions.size - 1

    val currentScreen: Screen
        get() = when {
            isAnalyzing -> Screen.LOADING
            missions.isEmpty() -> Screen.INPUT
            else -> Screen.RESULT
        }

    enum class Screen {
        INPUT,
        LOADING,
        RESULT
    }

    companion object {
        val FAKE = AutoMissionContract(
            noticeText = "내일은 독서록을 가져오세요. 수학 익힘책 30쪽까지 풀기.",
            missions = persistentListOf(
                MissionUiModel(
                    name = "독서록 챙기기",
                    dueAt = LocalDate.now().plusDays(1),
                    reward = 20
                ),
                MissionUiModel(
                    name = "수학 익힘책 풀기",
                    dueAt = LocalDate.now().plusDays(1),
                    reward = 50
                )
            ),
            currentIndex = 0
        )
    }
}