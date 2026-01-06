package com.kiero.presentation.parent.alarm.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.ParentTab
import com.kiero.presentation.parent.alarm.screen.ParentAlarmRoute

fun NavController.navigateToAlarm(
    navOptions: NavOptions? = null,
) {
    navigate(ParentTab.Alarm, navOptions)
}

fun NavGraphBuilder.parentAlarmNavGraph(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
) {
    composable<ParentTab.Alarm> {
        ParentAlarmRoute(
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
            navigateUp = navigateUp
        )
    }
}