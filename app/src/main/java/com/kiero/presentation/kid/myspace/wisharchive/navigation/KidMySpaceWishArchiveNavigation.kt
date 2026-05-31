package com.kiero.presentation.kid.myspace.wisharchive.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.myspace.wisharchive.KidMySpaceWishArchiveRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToWishArchive(
    navOptions: NavOptions? = null,
) {
    navigate(WishArchive, navOptions)
}

fun NavGraphBuilder.kidMySpaceWishArchiveNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToWish: () -> Unit,
) {
    composable<WishArchive> {
      KidMySpaceWishArchiveRoute(
          paddingValues = paddingValues,
          navigateUp = navigateUp,
          navigateToWish = navigateToWish
      )
    }
}

@Serializable
data object WishArchive : Route