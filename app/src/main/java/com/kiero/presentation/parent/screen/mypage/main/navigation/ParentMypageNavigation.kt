package com.kiero.presentation.parent.screen.mypage.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kiero.core.navigation.Route
import com.kiero.presentation.parent.navigation.Mypage
import com.kiero.presentation.parent.screen.mypage.childcare.ParentMyPageChildCareScreen
import com.kiero.presentation.parent.screen.mypage.childcare.navigation.MyPageChildCare
import com.kiero.presentation.parent.screen.mypage.main.ParentMyPageRoute
import com.kiero.presentation.parent.screen.mypage.main.route.licenses.OssLicensesScreen
import com.kiero.presentation.parent.screen.mypage.main.route.licenses.navigation.OssLicenses
import com.kiero.presentation.parent.screen.mypage.main.route.licenses.navigation.navigateToOssLicenses
import com.kiero.presentation.parent.screen.mypage.withdraw.ParentMyPageWithDrawScreen
import com.kiero.presentation.parent.screen.mypage.withdraw.navigation.MyPageWithDraw
import kotlinx.serialization.Serializable

@Serializable
data object MyPageGraph: Route

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
                navigateToOssLicenses = navController::navigateToOssLicenses
            )
        }

        composable<OssLicenses> {
            OssLicensesScreen(
                paddingValues = paddingValues,
                onBackClick = navigateUp
            )
        }

        composable<MyPageChildCare> {
            ParentMyPageChildCareScreen(
                paddingValues = paddingValues,
                navigateToMyPage = navController::navigateToMypage
            )
        }

        composable<MyPageWithDraw> {
            ParentMyPageWithDrawScreen(
                paddingValues = paddingValues,
                onBackClick = navigateUp
            )
        }
    }
}
