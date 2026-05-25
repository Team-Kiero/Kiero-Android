package com.kiero.presentation.parent.screen.mypage.childcare.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.kiero.core.navigation.Route
import kotlinx.serialization.Serializable

fun NavController.navigateToMyPageChildCare(
    navOptions: NavOptions? = null,
) {
    navigate(MyPageChildCare, navOptions)
}

@Serializable
data object MyPageChildCare : Route
