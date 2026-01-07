package com.kiero.presentation.parent.alarm.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.alarm.screen.ParentAlarmRoute
import com.kiero.presentation.parent.navigation.Alarm

fun NavController.navigateToAlarm(
    navOptions: NavOptions? = null,
) {
    navigate(Alarm, navOptions)
}

fun NavGraphBuilder.parentAlarmNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<Alarm> {
        ParentAlarmRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp
        )
    }
}