package com.kiero.presentation.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.kiero.core.navigation.Route
import com.kiero.presentation.auth.navigation.AuthGraph
import com.kiero.presentation.auth.navigation.authNavGraph
import com.kiero.presentation.kid.navigation.kidNavGraph
import com.kiero.presentation.parent.navigation.parentNavGraph

@Composable
fun KieroNavHost(
    appState: MainAppState,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    startDestination: Route = AuthGraph,
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        authNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues,
            navigateUp = appState::navigateUp,
            navigateToParent = appState::navigateToKidOnboarding,
            navigateToKid = appState::navigateToKid,
        )

        parentNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues,
            navigateUp = appState::navigateUp,
        )

        kidNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues,
            navigateUp = appState::navigateUp
        )
    }
}
