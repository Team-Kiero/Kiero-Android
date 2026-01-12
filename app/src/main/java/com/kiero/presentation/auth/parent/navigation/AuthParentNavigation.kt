package com.kiero.presentation.auth.parent.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.kiero.presentation.auth.navigation.Auth
import kotlinx.serialization.Serializable

@Serializable
data object ParentLogin : Auth

fun NavController.navigateToAuthParent(
    navOptions: NavOptions? = null
) {
    navigate(ParentLogin, navOptions)
}