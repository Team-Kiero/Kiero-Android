package com.kiero.presentation.main.navigation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
interface BottomBarTab {
    @get:DrawableRes val iconRes: Int
    @get:StringRes val contentDescription: Int
    @get:StringRes val labelRes: Int
}
