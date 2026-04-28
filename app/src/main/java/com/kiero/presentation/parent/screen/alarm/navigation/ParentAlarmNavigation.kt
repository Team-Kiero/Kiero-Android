package com.kiero.presentation.parent.screen.alarm.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.screen.alarm.screen.ParentAlarmRoute
import kotlinx.serialization.Serializable

@Serializable
data object Alarm : Route

fun NavController.navigateToAlarm(
    navOptions: NavOptions? = null,
) {
    navigate(Alarm, navOptions)
}

fun NavGraphBuilder.parentAlarmNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSelection: () -> Unit,
) {
    composable<Alarm> {
        ParentAlarmRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToSelection = navigateToSelection,

        )
    }
}
