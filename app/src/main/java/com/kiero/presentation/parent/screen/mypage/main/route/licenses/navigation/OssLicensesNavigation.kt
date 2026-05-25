package com.kiero.presentation.parent.screen.mypage.main.route.licenses.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.kiero.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object OssLicenses: Route

fun NavController.navigateToOssLicenses(
    navOptions: NavOptions? = navOptions {
        launchSingleTop = true
    }
) {
    navigate(
        route = OssLicenses,
        navOptions = navOptions
    )
}
