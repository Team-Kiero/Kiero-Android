package com.kiero.presentation.auth.parent.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kiero.presentation.auth.navigation.Auth
import com.kiero.presentation.auth.parent.AuthParentRoute
import kotlinx.serialization.Serializable

@Serializable
data object Login : Auth

fun NavGraphBuilder.AuthParentloginScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    composable<Login> {
        AuthParentRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            onLoginSuccess = onLoginSuccess
        )
    }
}