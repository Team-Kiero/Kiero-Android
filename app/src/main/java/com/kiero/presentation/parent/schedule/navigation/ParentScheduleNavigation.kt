package com.kiero.presentation.parent.schedule.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.ParentTab
import com.kiero.presentation.parent.schedule.screen.ParentScheduleRoute

fun NavController.navigateToSchedule(
    navOptions: NavOptions? = null,
) {
    navigate(ParentTab.Schedule, navOptions)
}

fun NavGraphBuilder.parentScheduleNavGraph(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
) {
    composable<ParentTab.Schedule> {
        ParentScheduleRoute(
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
            navigateUp = navigateUp,
        )
    }
}