package com.kiero.presentation.kid.mission.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.kid.mission.KidMissionRoute
import com.kiero.presentation.kid.navigation.Mission

fun NavController.navigateToMission(
    navOptions: NavOptions? = null,
) {
    navigate(Mission, navOptions)
}

fun NavGraphBuilder.kidMissionNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<Mission> {
        KidMissionRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}