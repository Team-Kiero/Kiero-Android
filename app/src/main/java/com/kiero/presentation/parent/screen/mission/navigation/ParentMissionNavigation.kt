package com.kiero.presentation.parent.screen.mission.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.navigation.ParentMission
import com.kiero.presentation.parent.screen.mission.ParentMissionRoute

fun NavController.navigateToParentMission(
    navOptions: NavOptions? = null,
) {
    navigate(ParentMission, navOptions)
}

fun NavGraphBuilder.parentMissionNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<ParentMission> {
        ParentMissionRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}