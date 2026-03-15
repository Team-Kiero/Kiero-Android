package com.kiero.presentation.parent.screen.reward.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.presentation.parent.navigation.ParentReward
import com.kiero.presentation.parent.screen.reward.ParentRewardRoute

fun NavController.navigateToReward(
    navOptions: NavOptions? = null,
) {
    navigate(ParentReward, navOptions)
}

fun NavGraphBuilder.parentRewardNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<ParentReward> {
        ParentRewardRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}