package com.kiero.presentation.kid.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.kid.navigation.Onboarding
import com.kiero.presentation.kid.onboarding.KidOnboardingRoute

fun NavController.navigateToKidOnboarding(
    navOptions: NavOptions? = null,
) {
    navigate(Onboarding, navOptions)
}

fun NavGraphBuilder.kidOnboardingNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToKid: () -> Unit,
) {
    composable<Onboarding> {
        KidOnboardingRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToKid = navigateToKid
        )
    }
}