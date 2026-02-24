package com.kiero.presentation.parent.mypage

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentMypageRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    ParentMypageScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp
    )
}

@Composable
private fun ParentMypageScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
}

@Preview
@Composable
private fun ParentMypageScreenPreview() {
    KieroTheme {
        ParentMypageScreen(
            paddingValues = PaddingValues(),
            navigateUp = {}
        )
    }
}