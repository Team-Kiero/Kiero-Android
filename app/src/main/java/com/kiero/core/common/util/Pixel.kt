package com.kiero.core.common.util

import android.content.res.Resources

// px을 dp로 변환
val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

// dp를 px로 변환
val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()