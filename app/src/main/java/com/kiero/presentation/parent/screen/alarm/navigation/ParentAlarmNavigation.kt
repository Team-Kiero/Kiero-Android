package com.kiero.presentation.parent.screen.alarm.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.screen.alarm.screen.ParentAlarmRoute
import kotlinx.serialization.Serializable

@Serializable
data class Alarm(
    val targetId: Long? = null,
    val expand: Boolean = false
) : Route

fun NavController.navigateToAlarm(
    targetId: Long? = null,
    expand: Boolean = false,
    navOptions: NavOptions? = null,
) {
    navigate(Alarm(targetId = targetId, expand = expand), navOptions)
}

fun NavGraphBuilder.parentAlarmNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSelection: () -> Unit,
) {
    composable<Alarm> { backStackEntry ->
        val alarmRoute = backStackEntry.toRoute<Alarm>()

        ParentAlarmRoute(
            targetId = alarmRoute.targetId,
            shouldExpand = alarmRoute.expand,
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToSelection = navigateToSelection,
        )
    }
}