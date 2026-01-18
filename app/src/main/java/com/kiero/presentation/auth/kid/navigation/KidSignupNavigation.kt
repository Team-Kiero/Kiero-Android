package com.kiero.presentation.auth.kid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.auth.kid.AuthKidSignupRoute
import com.kiero.presentation.auth.navigation.KidSignup

fun NavController.navigateToKidSignup(
    navOptions: NavOptions? = null,
) {
    navigate(KidSignup, navOptions)
}

fun NavGraphBuilder.AuthkidSignupNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToKidOnboarding: () -> Unit,
) {
    composable<KidSignup> {
        AuthKidSignupRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToKidOnboarding = navigateToKidOnboarding,
        )
    }
}