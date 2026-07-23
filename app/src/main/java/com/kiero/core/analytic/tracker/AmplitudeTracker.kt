package com.kiero.core.analytic.tracker

import com.amplitude.android.Amplitude
import com.amplitude.android.events.Identify
import com.kiero.core.analytic.type.AnalyticsEvent
import com.kiero.core.analytic.property.UserProperty
import javax.inject.Inject

class AmplitudeTracker @Inject constructor(
    private val amplitude: Amplitude,
) : Tracker {

    override fun track(event: AnalyticsEvent, properties: Map<String, Any?>) {
        amplitude.track(
            eventType = event.eventName,
            eventProperties = properties.filterValues { it != null } as Map<String, Any>,
        )
    }

    override fun setUserProperty(property: UserProperty) {
        val identify = Identify().set(property.key, property.value)
        amplitude.identify(identify)
    }
}