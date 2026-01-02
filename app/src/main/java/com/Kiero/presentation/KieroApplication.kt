package com.Kiero.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.Kiero.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class KieroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setTimber()
        setDayMode()
    }

    private fun setTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setDayMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}

