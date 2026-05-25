package com.kiero.presentation.parent.screen.mypage.withdraw.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.kiero.core.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPageWithDraw(
    navOptions: NavOptions? = null,
) {
    navigate(MyPageWithDraw, navOptions)
}

@Serializable
data object MyPageWithDraw: Route
