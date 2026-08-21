package com.kiero.core.analytic.di

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.AutocaptureOption
import com.amplitude.android.Configuration
import com.amplitude.android.TrackingOptions
import com.kiero.BuildConfig
import com.kiero.core.analytic.tracker.AmplitudeTracker
import com.kiero.core.analytic.tracker.Tracker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindTracker(tracker: AmplitudeTracker): Tracker

    companion object {
        @Provides
        @Singleton
        fun provideAmplitude(@ApplicationContext context: Context): Amplitude {
            return Amplitude(
                Configuration(
                    apiKey = BuildConfig.AMPLITUDE_API_KEY,
                    context = context,
                    autocapture = setOf(AutocaptureOption.SESSIONS),
                    trackingOptions = TrackingOptions()
                        .disableIpAddress()
                        .disableLatLng()
                        .disableCity()
                        .disableDma()
                        .disableCarrier()
                        .disableAdid()
                        .disableAppSetId(),
                )
            )
        }
    }
}
