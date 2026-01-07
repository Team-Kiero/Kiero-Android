package com.kiero.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kiero.core.navigation.AuthGraph
import com.kiero.core.navigation.Route
import com.kiero.presentation.auth.AuthRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface Auth : Route

@Serializable
data object Login : Auth

fun NavController.navigateToAuth(
    navOptions: NavOptions? = null,
) {
    navigate(Login, navOptions)
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
) {
    navigation<AuthGraph>(
        startDestination = Login
    ) {
        composable<Login> {
            AuthRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                navigateToParent = navigateToParent,
                navigateToKid = navigateToKid,
            )
        }
    }
}