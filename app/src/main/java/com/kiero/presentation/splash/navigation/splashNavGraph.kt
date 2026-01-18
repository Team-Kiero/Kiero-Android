package com.kiero.presentation.splash.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.splash.SplashRoute
import kotlinx.serialization.Serializable

fun NavGraphBuilder.splashNavGraph(
    navigateToAuth: () -> Unit,
    navigateToParentHome: () -> Unit,
    navigateToParentGraph: () -> Unit,
    navigateToKidHome: () -> Unit,
    navigateToKidOnboarding: () -> Unit,
) {
    composable<Splash>(
        exitTransition = { fadeOut(tween(500), 0.9999f) }
    ) {
        SplashRoute(
            navigateToAuth = navigateToAuth,
            navigateToParentHome = navigateToParentHome,
            navigateToKidHome = navigateToKidHome,
            navigateToParentGraph = navigateToParentGraph,
            navigateToKidOnboarding = navigateToKidOnboarding
        )
    }
}

@Serializable
data object Splash : Route
