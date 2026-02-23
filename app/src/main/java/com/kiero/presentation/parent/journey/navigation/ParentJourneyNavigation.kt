package com.kiero.presentation.parent.journey.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.journey.ParentJourneyRoute
import com.kiero.presentation.parent.navigation.ParentJourney

fun NavController.navigateToParentJourney(
    navOptions: NavOptions? = null,
) {
    navigate(ParentJourney, navOptions)
}

fun NavGraphBuilder.parentJourneyNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<ParentJourney> {
        ParentJourneyRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}