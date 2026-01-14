package com.kiero.presentation.kid.journey.fire.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.journey.fire.KidFireRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToFire(
    navOptions: NavOptions? = null,
) {
    navigate(Fire, navOptions)
}

fun NavGraphBuilder.kidJourneyFireNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<Fire> {
        KidFireRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}

@Serializable
data object Fire : Route