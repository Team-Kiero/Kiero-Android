package com.kiero.presentation.kid.journey.camera.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.journey.camera.KidCameraRoute
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType
import kotlinx.serialization.Serializable

fun NavController.navigateToCamera(
    scheduleDetailId: Long,
    stoneType: KidJourneyStoneType,
    navOptions: NavOptions? = null,
) {
    navigate(Camera(
        scheduleDetailId = scheduleDetailId,
        stoneType = stoneType
    ), navOptions)
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
data class Camera(
    val scheduleDetailId : Long,
    val stoneType: KidJourneyStoneType,
) : Route