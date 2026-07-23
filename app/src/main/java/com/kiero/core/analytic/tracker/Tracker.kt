package com.kiero.core.analytic.tracker

import com.kiero.core.analytic.type.AnalyticsEvent
import com.kiero.core.analytic.type.UserProperty

interface Tracker {
    fun track(event: AnalyticsEvent, properties: Map<String, Any?> = emptyMap())
    fun setUserProperty(property: UserProperty)
}