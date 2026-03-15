package com.kiero.presentation.kid.journey.map.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.journey.map.KidMapRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToMap(
    date: String,
    navOptions: NavOptions? = null
) {
    navigate(Map(date = date), navOptions)
}

fun NavGraphBuilder.kidJourneyMapNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<Map> {
        KidMapRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}

@Serializable
data class Map(
    val date: String
) : Route