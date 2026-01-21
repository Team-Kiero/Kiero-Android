package com.kiero.presentation.auth.kid.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.kiero.presentation.auth.navigation.KidSignup

fun NavController.navigateToKidSignup(
    navOptions: NavOptions? = null,
) {
    navigate(KidSignup, navOptions)
}
