package com.kiero.presentation.kid.journey.fire.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.journey.fire.KidFireResultRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToFireResult(
    navOptions: NavOptions? = null,
) {
    navigate(FireResult, navOptions)
}

fun NavGraphBuilder.kidJourneyFireResultNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<FireResult> {
        KidFireResultRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}

@Serializable
data object FireResult : Route