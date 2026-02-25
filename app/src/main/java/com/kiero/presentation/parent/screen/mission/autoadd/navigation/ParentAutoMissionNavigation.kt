package com.kiero.presentation.parent.screen.mission.autoadd.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.screen.mission.autoadd.ParentAutoAddRoute
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
    composable<AutoMissionAdd> {
        ParentAutoAddRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}

@Serializable
data class AutoMissionAdd(val childId: Long) : Route