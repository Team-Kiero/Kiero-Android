package com.kiero.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.kiero.core.navigation.Route
import com.kiero.presentation.auth.AuthRoute
import com.kiero.presentation.auth.kid.AuthKidSignupRoute
import com.kiero.presentation.auth.kid.navigation.navigateToKidSignup
import com.kiero.presentation.auth.parent.AuthParentRoute
import com.kiero.presentation.auth.parent.navigation.ParentLogin
import com.kiero.presentation.auth.parent.navigation.navigateToAuthParent
import com.kiero.presentation.kid.onboarding.navigation.navigateToKidOnboarding
import kotlinx.serialization.Serializable


interface Auth : Route

@Serializable
data object AuthGraph : Route

@Serializable
data object Selection : Auth

@Serializable
data object KidSignup : Auth

fun NavController.navigateToAuth(
    navOptions: NavOptions? = null,
) {
    navigate(Selection, navOptions)
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToParentGraph: () -> Unit,
    navigateToParentSignUp: (String, String) -> Unit,
    onEasterEggClick: () -> Unit
) {
    navigation<AuthGraph>(
        startDestination = Selection
    )  {
        composable<Selection> {
            AuthRoute(
                paddingValues = paddingValues,
                navigateToParent = navController::navigateToAuthParent,
                navigateToKid = navController::navigateToKidSignup,
                onEasterEggClick = onEasterEggClick
            )
        }

        composable<ParentLogin> {
            AuthParentRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToParentSignUp = { parentName, parentProfileImage ->
                    navigateToParentSignUp(parentName, parentProfileImage)
                },
                navigateToParentGraph = navigateToParentGraph
            )
        }

        composable<KidSignup> {
            AuthKidSignupRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToKidOnboarding = {
                    navController.navigateToKidOnboarding(
                        navOptions = navOptions {
                           popUpTo<AuthGraph> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    )
                }
            )
        }
    }
}