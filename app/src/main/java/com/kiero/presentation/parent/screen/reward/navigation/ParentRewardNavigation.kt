package com.kiero.presentation.parent.screen.reward.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.navigation.Reward
import com.kiero.presentation.parent.screen.reward.ParentRewardAddRoute
import com.kiero.presentation.parent.screen.reward.ParentRewardEditRoute
import com.kiero.presentation.parent.screen.reward.ParentRewardRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToReward(
    navOptions: NavOptions? = null,
) {
    navigate(Reward, navOptions)
}

fun NavController.navigateToRewardAdd(navOptions: NavOptions? = null) {
    navigate(RewardAdd, navOptions)
}

fun NavController.navigateToRewardEdit(couponId: Long, navOptions: NavOptions? = null) {
    navigate(RewardEdit(couponId), navOptions)
}

fun NavGraphBuilder.parentRewardNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToRewardAdd: () -> Unit,
    navigateToRewardEdit: (Long) -> Unit,
) {
    composable<Reward> {
        ParentRewardRoute(
            paddingValues = paddingValues,
            navigateToRewardAdd = navigateToRewardAdd,
            navigateToRewardEdit = navigateToRewardEdit,
        )
    }
    composable<RewardAdd> {
        ParentRewardAddRoute(
            navigateUp = navigateUp
        )
    }
    composable<RewardEdit> {
        ParentRewardEditRoute(navigateUp = navigateUp)
    }
}

@Serializable
data object RewardAdd : Route

@Serializable
data class RewardEdit(val couponId: Long) : Route