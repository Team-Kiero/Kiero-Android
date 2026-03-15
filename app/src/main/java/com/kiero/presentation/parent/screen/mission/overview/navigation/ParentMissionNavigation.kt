package com.kiero.presentation.parent.screen.mission.overview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.navigation.ParentMission
import com.kiero.presentation.parent.screen.mission.directadd.navigation.MissionEdit
import com.kiero.presentation.parent.screen.mission.overview.ParentMissionRoute

fun NavController.navigateToParentMission(
    navOptions: NavOptions? = null,
) {
    navigate(ParentMission, navOptions)
}

fun NavGraphBuilder.parentMissionNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToAddMission: () -> Unit,
    navigateToAutoMission: () -> Unit,
    navigateToMissionEdit: (MissionEdit) -> Unit,
) {
    composable<ParentMission> {
        ParentMissionRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToMissionEdit = navigateToMissionEdit,
            navigateToAddMission = navigateToAddMission,
            navigateToAutoMission = navigateToAutoMission,
        )
    }
}