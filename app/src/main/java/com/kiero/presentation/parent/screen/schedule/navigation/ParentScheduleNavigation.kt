package com.kiero.presentation.parent.screen.schedule.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.navigation.Schedule
import com.kiero.presentation.parent.screen.schedule.ParentScheduleRoute

fun NavController.navigateToSchedule(
    navOptions: NavOptions? = null,
) {
    navigate(Schedule, navOptions)
}

fun NavGraphBuilder.parentScheduleNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: (String, Boolean) -> Unit,
    navigateToSelection: () -> Unit,
    navigateToAlarm: () -> Unit,
) {
    composable<Schedule> {
        ParentScheduleRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToScheduleAdd = navigateToScheduleAdd,
            navigateToSelection = navigateToSelection,
            navigateToAlarm = navigateToAlarm,
        )
    }
}