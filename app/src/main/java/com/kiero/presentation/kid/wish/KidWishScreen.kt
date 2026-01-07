package com.kiero.presentation.kid.wish

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
        navigateUp = navigateUp
    )
}

@Composable
private fun KidWishScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
    ) {
        Text(
            text = "소원의 우물"
        )
    }
}

@Composable
@Preview
private fun KidWishScreenPreview() {
    KieroTheme {}
}