package com.kiero.presentation.kid.wish.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.kid.navigation.Wish
import com.kiero.presentation.kid.wish.KidWishRoute

fun NavController.navigateToWish(
    navOptions: NavOptions? = null,
) {
    navigate(Wish, navOptions)
}

fun NavGraphBuilder.kidWishNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<Wish> {
        KidWishRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}