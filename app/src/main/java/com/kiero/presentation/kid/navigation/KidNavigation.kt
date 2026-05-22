package com.kiero.presentation.kid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.journey.camera.navigation.kidJourneyCameraNavGraph
import com.kiero.presentation.kid.journey.camera.navigation.navigateToCamera
import com.kiero.presentation.kid.journey.fire.navigation.kidJourneyFireNavGraph
import com.kiero.presentation.kid.journey.fire.navigation.kidJourneyFireResultNavGraph
import com.kiero.presentation.kid.journey.fire.navigation.navigateToFire
import com.kiero.presentation.kid.journey.fire.navigation.navigateToFireResult
import com.kiero.presentation.kid.journey.map.navigation.kidJourneyMapNavGraph
import com.kiero.presentation.kid.journey.map.navigation.navigateToMap
import com.kiero.presentation.kid.journey.navigation.kidJourneyNavGraph
import com.kiero.presentation.kid.journey.navigation.navigateToJourney
import com.kiero.presentation.kid.mission.navigation.kidMissionNavGraph
import com.kiero.presentation.kid.myspace.navigation.kidMySpaceNavGraph
import com.kiero.presentation.kid.onboarding.navigation.kidOnboardingNavGraph
import com.kiero.presentation.kid.wish.navigation.kidWishNavGraph
import kotlinx.serialization.Serializable

sealed interface KidTab : Route

@Serializable
data object KidGraph : Route

@Serializable
data object KidJourney : KidTab

@Serializable
data object KidMission : KidTab

@Serializable
data object KidWish : KidTab

@Serializable
data object KidMySpace : KidTab

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
        startDestination = KidJourney
    ) {
        kidJourneyNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToCamera = { scheduleDetailId, stoneType ->
                navController.navigateToCamera(scheduleDetailId, stoneType)
            },
            navigateToFire = { date, stones->
                navController.navigateToFire(date, stones)
            },
            navigateToMap = { date ->
                navController.navigateToMap(date)
            }
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
            navigateToJourney = {
                val clearStackNavOptions = navOptions {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                navController.navigateToJourney(clearStackNavOptions)
            }
        )

        kidJourneyMapNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp
        )

        kidMySpaceNavGraph(
            paddingValues = paddingValues,
            navigateUp = navigateUp
        )
    }
}