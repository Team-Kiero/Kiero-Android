package com.kiero.presentation.parent.screen.mission.overview.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.mission.model.KidMissionByDateUiModel
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ParentMissionState(
    val kidMissionByDateList: KidMissionByDateUiModel = KidMissionByDateUiModel(
        missionsByDate = persistentListOf()
    ),
)
sealed interface ParentMissionSideEffect {
    data class ShowSnackbar(val message : String) : ParentMissionSideEffect
    data object RefreshMissions : ParentMissionSideEffect
}