package com.kiero.presentation.main.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val clearStackNavOptions = remember {
        navOptions {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

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
            navigateToAuth = {
                appState.navigateToAuth(clearStackNavOptions)
            },
            navigateToParentHome = {
                appState.navigateToSchedule(clearStackNavOptions)
            },
            navigateToKidHome = {
                appState.navigateToJourney(clearStackNavOptions)
            },
            navigateToParentGraph = {
                // 부모 카카오 로그인
                appState.navigateToAuthParent(clearStackNavOptions)
            },
            navigateToKidOnboarding = {
                appState.navigateToKidOnboarding(clearStackNavOptions)
            }
        )

        authNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues,
            navigateUp = appState::navigateUp,
            navigateToParentGraph = appState::navigateToParentGraph,
            navigateToParentSignUp = { parentName, parentProfileImage ->
                appState.navigateToParentSignUp(
                    parentName = parentName,
                    parentProfileImage = parentProfileImage,
                    navOptions = clearStackNavOptions
                )
            },
            navigateToSelection = appState::navigateToSelection,
            onEasterEggClick = appState::navigateToKidOnboarding
        )

        parentSignUpNavGraph(
            paddingValues = paddingValues,
            navigateToParent = {
                appState.navigateToClearParentGraph()
            },
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
