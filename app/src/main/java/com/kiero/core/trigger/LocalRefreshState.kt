package com.kiero.core.trigger

import androidx.compose.runtime.staticCompositionLocalOf
import com.kiero.core.model.trigger.RefreshState

val LocalRefreshState = staticCompositionLocalOf<RefreshState> {
    error("RefreshState not provided")
}