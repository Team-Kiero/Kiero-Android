package com.kiero.presentation.kid.journey.camera.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.journey.camera.KidCameraRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToCamera(
    navOptions: NavOptions? = null,
) {
    navigate(Camera, navOptions)
}

fun NavGraphBuilder.kidJourneyCameraNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<Camera> {
        KidCameraRoute(
            navigateUp = navigateUp
        )
    }
}

@Serializable
data object Camera : Route