package com.kiero.presentation.parent.screen.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.screen.mypage.ParentMypageRoute
import com.kiero.presentation.parent.navigation.Mypage

fun NavController.navigateToMypage(
    navOptions: NavOptions? = null,
) {
    navigate(Mypage, navOptions)
}

fun NavGraphBuilder.parentMypageNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<Mypage> {
        ParentMypageRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}