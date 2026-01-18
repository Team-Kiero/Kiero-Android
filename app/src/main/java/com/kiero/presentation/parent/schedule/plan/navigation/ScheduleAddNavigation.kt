package com.kiero.presentation.parent.schedule.plan.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.schedule.plan.ParentScheduleAddRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToScheduleAdd(
    navOptions: NavOptions? = null,
) {
    navigate(ScheduleAdd, navOptions)
}

fun NavGraphBuilder.parentScheduleAddNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<ScheduleAdd> {
        ParentScheduleAddRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}

@Serializable
data object ScheduleAdd : Route