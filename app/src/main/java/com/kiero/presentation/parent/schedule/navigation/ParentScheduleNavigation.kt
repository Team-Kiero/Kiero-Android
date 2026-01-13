package com.kiero.presentation.parent.schedule.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.navigation.Schedule
import com.kiero.presentation.parent.schedule.screen.ParentScheduleAddRoute
import com.kiero.presentation.parent.schedule.ParentScheduleRoute
import com.kiero.presentation.parent.schedule.plan.navigation.ScheduleAdd

fun NavController.navigateToSchedule(
    navOptions: NavOptions? = null,
) {
    navigate(Schedule, navOptions)
}

fun NavGraphBuilder.parentScheduleNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: () -> Unit,
) {
    composable<Schedule> {
        ParentScheduleRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToScheduleAdd = navigateToScheduleAdd
        )
    }

    composable<ScheduleAdd> {
        ParentScheduleAddRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}