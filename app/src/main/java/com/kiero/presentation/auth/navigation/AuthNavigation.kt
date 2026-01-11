package com.kiero.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kiero.core.navigation.Route
import com.kiero.presentation.auth.AuthRoute
import com.kiero.presentation.auth.KaKaoLoginRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface Auth : Route

@Serializable
data object AuthGraph : Route

@Serializable
data object Selection : Auth // 부모님/아이로 시작하기

@Serializable
data object Login : Auth

fun NavController.navigateToAuth(
    navOptions: NavOptions? = null,
) {
    navigate(Login, navOptions)
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToParent: () -> Unit,
    navigateToKid: () -> Unit,
) {
    // 시작점을 Selection으로 변경
    navigation<AuthGraph>(startDestination = Selection) {

        // 1. 역할 선택 (기존 AuthRoute)
        composable<Selection> {
            AuthRoute(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                // "부모님으로 시작" 클릭 시 진짜 로그인 화면(Login)으로 이동
                navigateToParent = { navController.navigate(Login) },
                navigateToKid = navigateToKid,
            )
        }

        // 2. 카카오 로그인 화면
        composable<Login> {
            KaKaoLoginRoute(
                paddingValues = paddingValues,
                navigateUp = { navController.popBackStack() },
                onLoginSuccess = navigateToParent // 로그인 성공 시 최종 부모 메인으로 이동!
            )
        }
    }
}