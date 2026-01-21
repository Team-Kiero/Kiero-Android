package com.kiero.presentation.main.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.network.monitor.NetworkMonitor
import com.kiero.presentation.main.navigation.rememberMainAppState
import com.kiero.presentation.main.screen.MainRoute
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(
                getColor(R.color.bottombar_color)
            )
        )
        setContent {
            KieroTheme {
                val appState = rememberMainAppState(networkMonitor = networkMonitor)

                MainRoute(
                    appState = appState
                )
            }
        }
    }
}
