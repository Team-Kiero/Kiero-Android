package com.kiero.presentation.kid.myspace.policy.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.kiero.core.navigation.Route
import com.kiero.presentation.kid.myspace.policy.KidMySpacePolicyRoute
import com.kiero.presentation.parent.screen.mypage.main.route.licenses.OssLicensesScreen
import com.kiero.presentation.parent.screen.mypage.main.route.licenses.navigation.OssLicenses
import com.kiero.presentation.parent.screen.mypage.main.route.licenses.navigation.navigateToOssLicenses
import kotlinx.serialization.Serializable

fun NavController.navigateToPolicy(
    navOptions: NavOptions? = null,
) {
    navigate(KidMySpacePolicy, navOptions)
}

fun NavGraphBuilder.kidMySpacePolicyNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
) {
    composable<KidMySpacePolicy> {
        KidMySpacePolicyRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToOssLicenses = navController::navigateToOssLicenses
        )
    }

    composable<OssLicenses> {
        OssLicensesScreen(
            paddingValues = paddingValues,
            onBackClick = navigateUp
        )
    }
}

@Serializable
data object KidMySpacePolicy : Route