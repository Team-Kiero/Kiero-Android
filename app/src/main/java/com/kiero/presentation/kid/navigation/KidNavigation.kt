package com.kiero.presentation.kid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.journey.camera.navigation.kidJourneyCameraNavGraph
import com.kiero.presentation.kid.journey.camera.navigation.navigateToCamera
import com.kiero.presentation.kid.journey.fire.navigation.kidJourneyFireNavGraph
import com.kiero.presentation.kid.journey.fire.navigation.kidJourneyFireResultNavGraph
import com.kiero.presentation.kid.journey.fire.navigation.navigateToFire
import com.kiero.presentation.kid.journey.fire.navigation.navigateToFireResult
import com.kiero.presentation.kid.journey.navigation.kidJourneyNavGraph
import com.kiero.presentation.kid.mission.navigation.kidMissionNavGraph
import com.kiero.presentation.kid.onboarding.navigation.kidOnboardingNavGraph
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

@Serializable
data object Onboarding : KidTab

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
            navigateToCamera = navController::navigateToCamera,
            navigateToFire = navController::navigateToFire
        )

        kidMissionNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )

        kidWishNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
        
        kidOnboardingNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToKid = navController::navigateToKid
        )

        kidJourneyCameraNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp
        )

        kidJourneyFireNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToFireResult = navController::navigateToFireResult
        )

        kidJourneyFireResultNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}