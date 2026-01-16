package com.kiero.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
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
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
) {
    navigation<AuthGraph>(startDestination = Selection) {

        composable<Selection> {
            AuthRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToParent = navController::navigateToAuthParent,
                navigateToKid = navController::navigateToKidSignup,
                navigateToEx = navigateToParent
            )
        }

        composable<ParentLogin> {
            AuthParentRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
            )
        }

        composable<KidSignup> {
            AuthKidSignupRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToKidOnboarding = navController::navigateToKidOnboarding
            )
        }
    }
}