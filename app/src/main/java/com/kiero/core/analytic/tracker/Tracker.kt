package com.kiero.core.analytic.tracker

import com.kiero.core.analytic.property.UserProperty
import com.kiero.core.analytic.type.AnalyticsEvent

interface Tracker {
    fun track(event: AnalyticsEvent, properties: Map<String, Any?>)
    fun setUserProperty(property: UserProperty)
    fun setUserId(userId: String?)
}