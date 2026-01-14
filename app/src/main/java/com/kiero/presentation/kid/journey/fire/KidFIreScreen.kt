package com.kiero.presentation.kid.journey.fire

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidFireRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    KidFIreScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
    )
}

@Composable
private fun KidFIreScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

}

@Composable
@Preview
private fun KidFIreScreenPreview() {
    KieroTheme {
        KidFIreScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
        )
    }
}