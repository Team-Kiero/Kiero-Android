package com.kiero.presentation.parent.schedule.screen

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
fun ParentScheduleRoute(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateUp: () -> Unit,
) {
    ParentScheduleScreen(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        navigateUp = navigateUp
    )
}

@Composable
private fun ParentScheduleScreen(
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
            text = "스케쥴 관리"
        )
    }
}

@Composable
@Preview
private fun ParentScheduleScreenPreview() {
    KieroTheme {}
}