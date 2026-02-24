package com.kiero.presentation.parent.reward

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme


@Composable
fun ParentRewardRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    ParentRewardScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp
    )
}

@Composable
private fun ParentRewardScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
}

@Preview
@Composable
private fun ParentRewardScreenPreview() {
    KieroTheme{
        ParentRewardScreen(
            paddingValues = PaddingValues(),
            navigateUp = {}
        )
    }
}