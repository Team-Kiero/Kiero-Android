package com.kiero.presentation.kid.wish

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidWishRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    KidWishScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
    )
}

@Composable
private fun KidWishScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

}

@Composable
@Preview
private fun KidWishScreenPreview() {
    KieroTheme {
        KidWishScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
        )
    }
}