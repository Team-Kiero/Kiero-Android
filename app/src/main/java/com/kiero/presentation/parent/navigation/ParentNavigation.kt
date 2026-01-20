package com.kiero.presentation.parent.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.alarm.navigation.parentAlarmNavGraph
import com.kiero.presentation.parent.schedule.mission.navigation.navigateToMissionAdd
import com.kiero.presentation.parent.schedule.mission.navigation.parentMissionAddNavGraph
import com.kiero.presentation.parent.schedule.navigation.parentScheduleNavGraph
import com.kiero.presentation.parent.schedule.plan.navigation.navigateToScheduleAdd
import com.kiero.presentation.parent.schedule.plan.navigation.parentScheduleAddNavGraph
import com.kiero.presentation.parent.schedule.mission.auto.navigation.navigateToAutoMissionAdd
import com.kiero.presentation.parent.schedule.mission.auto.navigation.parentAutoMissionAddNavGraph
import kotlinx.serialization.Serializable

sealed interface ParentTab : Route

@Serializable
data object ParentGraph : Route

@Serializable
data object Schedule : ParentTab

@Serializable
data object Alarm : ParentTab

fun NavController.navigateToParent(
    navOptions: NavOptions? = null,
) {
    navigate(ParentGraph, navOptions)
}

fun NavGraphBuilder.parentNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSelection: () -> Unit
) {
    navigation<ParentGraph>(
        startDestination = Schedule
    ) {
        parentScheduleNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToScheduleAdd = navController::navigateToScheduleAdd,
            navigateToMissionAdd = navController::navigateToMissionAdd,
            navigateToAutoMissionAdd = navController::navigateToAutoMissionAdd,
            navigateToSelection = navigateToSelection
        )

        parentScheduleAddNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )

        parentMissionAddNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )

        parentAutoMissionAddNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp
        )

        parentAlarmNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}
