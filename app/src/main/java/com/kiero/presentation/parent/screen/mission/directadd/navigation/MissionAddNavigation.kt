package com.kiero.presentation.parent.screen.mission.directadd.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.screen.mission.directadd.ParentAddMissionRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMissionAdd(
    navOptions: NavOptions? = null,
) {
    navigate(MissionAdd, navOptions)
}

fun NavGraphBuilder.parentMissionAddNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<MissionAdd> {
        ParentAddMissionRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}


@Serializable
data object MissionAdd : Route


@Serializable
data class MissionEdit(
    val missionId: Long = -1L,
    val name: String = "",
    val reward: Int = 0,
    val dueAt: String = "",
)
