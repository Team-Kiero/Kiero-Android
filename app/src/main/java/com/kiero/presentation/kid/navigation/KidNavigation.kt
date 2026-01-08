package com.kiero.presentation.kid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.journey.navigation.kidJourneyNavGraph
import com.kiero.presentation.kid.mission.navigation.kidMissionNavGraph
import com.kiero.presentation.kid.wish.navigation.kidWishNavGraph
import kotlinx.serialization.Serializable

sealed interface KidTab : Route

@Serializable
data object KidGraph : Route

@Serializable
data object Journey : KidTab

@Serializable
data object Mission : KidTab

@Serializable
data object Wish : KidTab


fun NavController.navigateToKid(
    navOptions: NavOptions? = null,
) {
    navigate(KidGraph, navOptions)
}

fun NavGraphBuilder.kidNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    navigation<KidGraph>(
        startDestination = Journey
    ) {
        kidJourneyNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )

        kidMissionNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )

        kidWishNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}