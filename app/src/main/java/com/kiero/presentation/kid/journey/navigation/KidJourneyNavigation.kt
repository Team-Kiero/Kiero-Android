package com.kiero.presentation.kid.journey.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.kid.journey.KidJourneyRoute
import com.kiero.presentation.kid.navigation.Journey

fun NavController.navigateToJourney(
    navOptions: NavOptions? = null,
) {
    navigate(Journey, navOptions)
}

fun NavGraphBuilder.kidJourneyNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCamera: () -> Unit,
    navigateToFire: () -> Unit
) {
    composable<Journey> {
        KidJourneyRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToCamera = navigateToCamera,
            navigateToFire = navigateToFire
        )
    }
}