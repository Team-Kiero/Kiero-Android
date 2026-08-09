package com.kiero.core.analytic.type

interface AnalyticsEvent {
    val type: EventType
    val eventName: String
    val properties: Map<String, Any?> get() = emptyMap()
}