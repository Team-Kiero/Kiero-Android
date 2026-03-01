package com.kiero.core.designsystem.component.animation

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

sealed interface KieroAnimationType {
    data class Lottie(@param: RawRes val resId: Int) : KieroAnimationType
    data class Image(@param: DrawableRes val resId: Int) : KieroAnimationType
}