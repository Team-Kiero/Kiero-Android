package com.kiero.presentation.parent.screen.schedule.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.navigation.ParentSchedule
import com.kiero.presentation.parent.screen.schedule.ParentScheduleRoute
import com.kiero.presentation.parent.screen.schedule.plan.navigation.ScheduleEdit

fun NavController.navigateToSchedule(
    navOptions: NavOptions? = null,
) {
    navigate(ParentSchedule, navOptions)
}

fun NavGraphBuilder.parentScheduleNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: (String, Boolean) -> Unit,
    navigateToScheduleEdit: (ScheduleEdit) -> Unit,
    navigateToSelection: () -> Unit,
    navigateToAlarm: () -> Unit,
) {
    composable<ParentSchedule> {
        ParentScheduleRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToScheduleAdd = navigateToScheduleAdd,
            navigateToScheduleEdit = navigateToScheduleEdit,
            navigateToAlarm = navigateToAlarm,
        )
    }
}