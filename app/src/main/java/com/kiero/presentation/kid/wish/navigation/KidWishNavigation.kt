package com.kiero.presentation.kid.wish.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.kid.navigation.KidWish
import com.kiero.presentation.kid.wish.KidWishRoute

fun NavController.navigateToWish(
    navOptions: NavOptions? = null,
) {
    navigate(KidWish, navOptions)
}

fun NavGraphBuilder.kidWishNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    onWishArchiveClick: () -> Unit,
) {
    composable<KidWish> {
        KidWishRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            onWishArchiveClick = onWishArchiveClick
        )
    }
}