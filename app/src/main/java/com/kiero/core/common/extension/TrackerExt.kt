package com.kiero.core.common.extension

import com.kiero.core.analytic.tracker.Tracker
import com.kiero.core.analytic.type.AnalyticsEvent

fun Tracker.track(event: AnalyticsEvent) {
    track(event = event, properties = event.properties)
}