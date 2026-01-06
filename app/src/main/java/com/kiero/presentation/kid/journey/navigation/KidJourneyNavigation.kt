package com.kiero.presentation.kid.journey

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.KidTab

fun NavController.navigateToJourney(
    navOptions: NavOptions? = null,
) {
    navigate(KidTab.Journey, navOptions)
}

fun NavGraphBuilder.kidJourneyNavGraph(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
) {
    composable<KidTab.Journey> {
        KidJourneyRoute(
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
            navigateUp = navigateUp,
        )
    }
}