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
    date: String,
    stones: Int,
    navOptions: NavOptions? = null
) {
    navigate(Fire(
        date = date,
        stones = stones
    ), navOptions)
}

fun NavGraphBuilder.kidJourneyFireNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToFireResult: (String) -> Unit,
) {
    composable<Fire> {
        KidFireRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToFireResult = { date ->
                navigateToFireResult(date)
            }
        )
    }
}

@Serializable
data class Fire(
    val date: String,
    val stones: Int
) : Route