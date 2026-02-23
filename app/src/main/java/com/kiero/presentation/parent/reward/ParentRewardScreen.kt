package com.kiero.presentation.parent.reward

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


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
    ParentRewardScreen(
        paddingValues = PaddingValues(),
        navigateUp = {}
    )
}