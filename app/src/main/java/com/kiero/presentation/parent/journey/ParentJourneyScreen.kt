package com.kiero.presentation.parent.journey

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentJourneyRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    ParentJourneyScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp
    )
}

@Composable
private fun ParentJourneyScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
}

@Preview
@Composable
private fun ParentJourneyScreenPreview() {
    KieroTheme {
        ParentJourneyScreen(
            paddingValues = PaddingValues(),
            navigateUp = {}
        )
    }
}