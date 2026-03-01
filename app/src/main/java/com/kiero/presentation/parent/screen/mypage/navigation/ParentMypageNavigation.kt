package com.kiero.presentation.parent.screen.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.navigation.ParentMypage
import com.kiero.presentation.parent.screen.mypage.ParentMypageRoute

fun NavController.navigateToMypage(
    navOptions: NavOptions? = null,
) {
    navigate(ParentMypage, navOptions)
}

fun NavGraphBuilder.parentMypageNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<ParentMypage> {
        ParentMypageRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}