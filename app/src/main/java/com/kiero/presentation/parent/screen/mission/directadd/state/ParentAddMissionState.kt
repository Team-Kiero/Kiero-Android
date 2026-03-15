package com.kiero.presentation.parent.screen.mission.directadd.state

import androidx.compose.runtime.Immutable
import com.kiero.data.parent.mission.model.ParentMissionAddModel

@Immutable
data class ParentAddMissionState(
    val isLoading: Boolean = false,
    val childId: Long? = null,
    val missionName: String = "",
    val reward: Int = 50,
    val selectedDate: String? = null,
) {
    val isValidInput: Boolean
        get() = missionName.isNotBlank() &&
                selectedDate != null &&
                childId != null &&
                reward > 0
}

sealed interface ParentAddMissionSideEffect {
    data class ShowSnackbar(val message: String) : ParentAddMissionSideEffect

    data class NavigateToMissionList(
        val mission: ParentMissionAddModel
    ) : ParentAddMissionSideEffect

    data object NavigateUp : ParentAddMissionSideEffect
}