package com.kiero.presentation.kid.mission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidMissionRoute(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
) {
    KidMissionScreen(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        navigateUp = navigateUp
    )
}

@Composable
private fun KidMissionScreen(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
    ) {
        Text(
            text = "금화 미션"
        )
    }
}

@Composable
@Preview
private fun KidMissionScreenPreview() {
    KieroTheme {}
}