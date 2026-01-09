package com.kiero.presentation.main.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
