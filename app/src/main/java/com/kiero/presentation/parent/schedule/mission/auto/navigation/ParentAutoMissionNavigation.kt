package com.kiero.presentation.parent.schedule.mission.auto.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.schedule.mission.auto.ParentAutoAddRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToAutoMissionAdd(
    childId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(AutoMissionAdd(childId), navOptions)
}

fun NavGraphBuilder.parentAutoMissionAddNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<AutoMissionAdd> { backStackEntry ->
        val route = backStackEntry.toRoute<AutoMissionAdd>()

        ParentAutoAddRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            childId = route.childId
        )
    }
}

@Serializable
data class AutoMissionAdd(val childId: Long) : Route