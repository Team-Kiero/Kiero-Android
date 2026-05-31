package com.kiero.presentation.main.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kiero.core.common.extension.toPushDataOrNull
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.network.monitor.NetworkMonitor
import com.kiero.core.model.fcm.PushData
import com.kiero.presentation.main.navigation.rememberMainAppState
import com.kiero.presentation.main.screen.MainRoute
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private var pendingPushData by mutableStateOf<PushData?>(null)

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(scrim = Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(scrim = Color.TRANSPARENT)
        )
        pendingPushData = intent.toPushDataOrNull()

        setContent {
            KieroTheme {
                val appState = rememberMainAppState(networkMonitor = networkMonitor)

                LaunchedEffect(pendingPushData) {
                    pendingPushData?.let { pushData ->
                        appState.navigateFromPushData(pushData)
                        pendingPushData = null
                    }
                }
                MainRoute(appState = appState)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingPushData = intent.toPushDataOrNull()
    }
}