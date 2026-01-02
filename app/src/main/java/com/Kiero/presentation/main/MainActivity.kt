package com.Kiero.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.Kiero.core.designsystem.theme.KieroTheme
import com.Kiero.presentation.auth.AuthRoute
import com.Kiero.presentation.auth.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KieroTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    viewModel.getDummyList()
                }

                AuthRoute(
                    paddingValues = PaddingValues(),
                    navigateUp = { viewModel.navigateUp() },
                    navigateNext = { viewModel.navigateNext() },
                    state = state.uiState
                )
            }
        }
    }
}