package com.kiero.presentation.main.navigation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface BottomBarTab {
    @get:DrawableRes val iconRes: Int
    @get:StringRes val contentDescription: Int
    @get:StringRes val labelRes: Int
}