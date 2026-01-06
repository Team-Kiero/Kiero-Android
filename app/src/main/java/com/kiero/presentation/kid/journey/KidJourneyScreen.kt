package com.kiero.presentation.kid.journey

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
fun KidJourneyRoute(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
) {
    KidJourneyScreen(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        navigateUp = navigateUp
    )
}

@Composable
private fun KidJourneyScreen(
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
            text = "오늘의 여정"
        )
    }
}

@Composable
@Preview
private fun KidJourneyScreenPreview() {
    KieroTheme {}
}