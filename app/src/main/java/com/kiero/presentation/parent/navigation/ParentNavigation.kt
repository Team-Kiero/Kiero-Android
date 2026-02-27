package com.kiero.presentation.parent.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.screen.alarm.navigation.parentAlarmNavGraph
import com.kiero.presentation.parent.screen.journey.navigation.parentJourneyNavGraph
import com.kiero.presentation.parent.screen.mission.auto.navigation.parentAutoMissionAddNavGraph
import com.kiero.presentation.parent.screen.mission.navigation.parentMissionAddNavGraph
import com.kiero.presentation.parent.screen.mission.navigation.parentMissionNavGraph
import com.kiero.presentation.parent.screen.mypage.navigation.parentMypageNavGraph
import com.kiero.presentation.parent.screen.reward.navigation.parentRewardNavGraph
import com.kiero.presentation.parent.screen.schedule.navigation.parentScheduleNavGraph
import com.kiero.presentation.parent.screen.schedule.plan.navigation.navigateToScheduleAdd
import com.kiero.presentation.parent.screen.schedule.plan.navigation.parentScheduleAddNavGraph
import kotlinx.serialization.Serializable

sealed interface ParentTab : Route

@Serializable
data object ParentGraph : Route

@Serializable
data object Schedule : ParentTab

@Serializable
data object Alarm : ParentTab

@Serializable
data object ParentJourney : ParentTab

@Serializable
data object ParentMission : ParentTab

@Serializable
data object Reward : ParentTab

@Serializable
data object Mypage : ParentTab


fun NavController.navigateToParent(
    navOptions: NavOptions? = null,
) {
    navigate(ParentGraph, navOptions)
}

fun NavGraphBuilder.parentNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSelection: () -> Unit,
    navigateToAlarm: () -> Unit,
) {
    navigation<ParentGraph>(
        startDestination = Schedule
    ) {
        parentScheduleNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToScheduleAdd = { date, fireLit ->
                navController.navigateToScheduleAdd(
                    initialDate = date,
                    isFireLit = fireLit
                )
            },
            navigateToSelection = navigateToSelection,
            navigateToAlarm = navigateToAlarm
        )

        parentScheduleAddNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )

        parentMissionAddNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )

        parentMissionNavGraph(
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
            navigateToSelection = navigateToSelection
        )

        parentJourneyNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )

        parentRewardNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )

        parentMypageNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}
