package com.kiero.presentation.main.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.kiero.core.navigation.Route
import com.kiero.presentation.auth.navigation.AuthGraph
import com.kiero.presentation.auth.navigation.authNavGraph
import com.kiero.presentation.kid.navigation.kidNavGraph
import com.kiero.presentation.parent.navigation.parentNavGraph
import com.kiero.presentation.signup.parent.navigation.parentSignUpNavGraph
import com.kiero.presentation.splash.navigation.splashNavGraph
import timber.log.Timber

@Composable
fun KieroNavHost(
    appState: MainAppState,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    startDestination: Route = AuthGraph,
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        modifier = modifier
    ) {
        splashNavGraph(
            navigateToAuth = appState::navigateToAuth,
            navigateToParentHome = {
                Timber.e("navigateToParentHome")
                val clearStackNavOptions = navOptions {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                appState.navigateToSchedule(
                    navOptions = clearStackNavOptions
                )
            },
            navigateToKidHome = {
                Timber.e("navigateToKidHome")
                val clearStackNavOptions = navOptions {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                appState.navigateToJourney(clearStackNavOptions)
            },
            navigateToParentGraph = {
                Timber.e("navigateToAuthParent")
                val clearStackNavOptions = navOptions {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                appState.navigateToAuthParent(clearStackNavOptions)
            },
            navigateToKidOnboarding = {
                Timber.e("navigateToKidOnboarding")
                val clearStackNavOptions = navOptions {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                appState.navigateToKidOnboarding(clearStackNavOptions)
            }
        )

        authNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues,
            navigateUp = appState::navigateUp,
            navigateToParentGraph = appState::navigateToParentGraph,
            navigateToParentSignUp = appState::navigateToParentSignUp,
            onEasterEggClick = appState::navigateToKidOnboarding
        )

        parentSignUpNavGraph(
            paddingValues = paddingValues,
            navigateToParent = appState::navigateToParentGraph,
            navigateToSelection = appState::navigateToSelection
        )



        parentNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues,
            navigateUp = appState::navigateUp,
            navigateToSelection = appState::navigateToSelection
        )

        kidNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues,
            navigateUp = appState::navigateUp
        )
    }
}
