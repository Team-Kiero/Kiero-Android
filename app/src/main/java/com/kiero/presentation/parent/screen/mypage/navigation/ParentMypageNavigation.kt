package com.kiero.presentation.parent.screen.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kiero.presentation.parent.screen.mypage.ParentMyPageRoute
import com.kiero.presentation.parent.screen.mypage.route.licenses.OssLicensesScreen
import com.kiero.presentation.parent.screen.mypage.route.licenses.navigation.OssLicenses
import com.kiero.presentation.parent.screen.mypage.route.licenses.navigation.navigateToOssLicenses
import kotlinx.serialization.Serializable
import com.kiero.presentation.parent.navigation.Mypage

@Serializable
data object MyPageGraph

fun NavController.navigateToMypage(
    navOptions: NavOptions? = null,
) {
    navigate(Mypage, navOptions)
}

fun NavGraphBuilder.parentMypageNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
) {
    navigation<MyPageGraph> (
        startDestination = Mypage
    ) {
        composable<Mypage> {
            ParentMyPageRoute(
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
}
