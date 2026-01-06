package com.kiero.presentation.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.kiero.core.navigation.Auth
import com.kiero.core.navigation.AuthGraph
import com.kiero.core.navigation.KidGraph
import com.kiero.core.navigation.KidTab
import com.kiero.core.navigation.ParentGraph
import com.kiero.core.navigation.ParentTab
import com.kiero.core.navigation.Route
import com.kiero.presentation.auth.navigation.authNavGraph
import com.kiero.presentation.kid.journey.kidJourneyNavGraph
import com.kiero.presentation.kid.mission.kidMissionNavGraph
import com.kiero.presentation.kid.wish.kidWishNavGraph
import com.kiero.presentation.parent.alarm.navigation.parentAlarmNavGraph
import com.kiero.presentation.parent.schedule.navigation.parentScheduleNavGraph

@Composable
fun KieroNavHost(
    navigator: MainNavigator,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    startDestination: Route = AuthGraph,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navigation<AuthGraph>(
            startDestination = Auth.Login
        ) {
            authNavGraph(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                navigateUp = navigator::navigateUp,
                navigateToParent = navigator::navigateToParent,
                navigateToKid = navigator::navigateToKid,
            )
        }

        navigation<ParentGraph>(
            startDestination = ParentTab.Schedule
        ) {
            parentScheduleNavGraph(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                navigateUp = navigator::navigateUp,
            )

            parentAlarmNavGraph(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                navigateUp = navigator::navigateUp
            )
        }

        navigation<KidGraph>(
            startDestination = KidTab.Journey
        ) {
            kidJourneyNavGraph(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                navigateUp = navigator::navigateUp,
            )

            kidMissionNavGraph(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                navigateUp = navigator::navigateUp,
            )

            kidWishNavGraph(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                navigateUp = navigator::navigateUp,
            )
        }
    }
}