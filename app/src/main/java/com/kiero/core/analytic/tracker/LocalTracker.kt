package com.kiero.core.analytic.tracker

import androidx.compose.runtime.staticCompositionLocalOf

val LocalTracker = staticCompositionLocalOf<Tracker> {
    error("Tracker not provided! Make sure to wrap your app with CompositionLocalProvider.")
}