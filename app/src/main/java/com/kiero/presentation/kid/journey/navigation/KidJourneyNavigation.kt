package com.kiero.presentation.kid.journey.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.kid.journey.KidJourneyRoute
import com.kiero.presentation.kid.navigation.KidJourney
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType


fun NavController.navigateToJourney(
    navOptions: NavOptions? = null,
) {
    navigate(KidJourney, navOptions)
}

fun NavGraphBuilder.kidJourneyNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCamera: (Long, KidJourneyStoneType) -> Unit,
    navigateToFire: (String, Int) -> Unit,
    navigateToMap: (String) -> Unit
) {
    composable<KidJourney> {
        KidJourneyRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToCamera = { scheduleDetailId, stoneType ->
                navigateToCamera(scheduleDetailId, stoneType)
            },
            navigateToFire = { date, stones ->
                navigateToFire(date, stones)
            },
            navigateToMap = { date ->
                navigateToMap(date)
            }
        )
    }
}