package com.kiero.presentation.kid.myspace.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.kid.myspace.KidMySpaceRoute
import com.kiero.presentation.kid.navigation.KidMySpace

fun NavController.navigateToMySpace(
    navOptions: NavOptions? = null,
) {
    navigate(KidMySpace, navOptions)
}

fun NavGraphBuilder.kidMySpaceNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToWishArchive: () -> Unit
) {
    composable<KidMySpace> {
        KidMySpaceRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToWishArchive = navigateToWishArchive
        )
    }
}