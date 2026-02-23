package com.kiero.presentation.parent.mission.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.mission.model.KidMissionByDateUiModel

@Immutable
data class ParentMissionState(
    val kidName: String = "",

    val kidMissionByDateList: KidMissionByDateUiModel = KidMissionByDateUiModel()
)

sealed interface ParentMissionSideEffect {
    data class ShowSnackbar(val message : String) : ParentMissionSideEffect
}