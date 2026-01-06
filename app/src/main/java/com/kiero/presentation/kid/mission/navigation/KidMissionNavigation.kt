package com.kiero.presentation.kid.mission

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.KidTab

fun NavController.navigateToMission(
    navOptions: NavOptions? = null,
) {
    navigate(KidTab.Mission, navOptions)
}

fun NavGraphBuilder.kidMissionNavGraph(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
) {
    composable<KidTab.Mission> {
        KidMissionRoute(
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
            navigateUp = navigateUp,
        )
    }
}