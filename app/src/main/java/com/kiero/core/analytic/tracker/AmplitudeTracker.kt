package com.kiero.core.analytic.tracker

import com.amplitude.android.Amplitude
import com.amplitude.android.events.Identify
import com.kiero.core.analytic.property.UserProperty
import com.kiero.core.analytic.type.AnalyticsEvent
import javax.inject.Inject

class AmplitudeTracker @Inject constructor(
    private val amplitude: Amplitude,
) : Tracker {

    override fun track(event: AnalyticsEvent, properties: Map<String, Any?>) {
        amplitude.track(
            eventType = event.eventName,
            eventProperties = buildMap {
                properties.forEach { (key, value) ->
                    if (value != null) put(key, value)
                }
            }
        )
    }

    override fun setUserProperty(property: UserProperty) {
        val identify = Identify().set(property.key, property.value)
        amplitude.identify(identify)
    }

    override fun setUserId(userId: String?) {
        amplitude.setUserId(userId)
    }
}