package com.kiero.presentation.parent.screen.mypage.withdraw.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.kiero.core.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPageWithdraw(
    navOptions: NavOptions? = null,
) {
    navigate(MyPageWithdraw, navOptions)
}

@Serializable
data object MyPageWithdraw: Route
