package com.kiero.presentation.parent.screen.mission.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.screen.mission.ParentAddMissionRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMissionAdd(
    navOptions: NavOptions? = null,
) {
    navigate(MissionAdd, navOptions)
}

fun NavGraphBuilder.parentMissionAddNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<MissionAdd> {
        ParentAddMissionRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}


@Serializable
data object MissionAdd : Route