package com.kiero.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.model.UiState
import com.kiero.core.navigation.Auth
import com.kiero.presentation.auth.AuthRoute

fun NavController.navigateToAuth(
    navOptions: NavOptions? = null,
) {
    navigate(Auth.Login, navOptions)
}

fun NavGraphBuilder.authNavGraph(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
) {
    composable<Auth.Login> {
        AuthRoute(
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
            navigateUp = navigateUp,
            navigateToParent = navigateToParent,
            navigateToKid = navigateToKid,
            state = UiState.Loading
        )
    }
}