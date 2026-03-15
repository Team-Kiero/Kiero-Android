package com.kiero.presentation.signup.parent.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.signup.parent.ParentSignUpRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToParentSignUp(
    navOptions: NavOptions? = null,
) {
    navigate(ParentSignUp, navOptions)
}

fun NavGraphBuilder.parentSignUpNavGraph(
    paddingValues: PaddingValues,
    navigateToParent: () -> Unit,
    navigateToSelection: () -> Unit
) {
    composable<ParentSignUp> {
        ParentSignUpRoute(
            paddingValues = paddingValues,
            navigateToParent = navigateToParent,
            navigateToSelection = navigateToSelection
        )
    }
}

@Serializable
data object ParentSignUp: Route
