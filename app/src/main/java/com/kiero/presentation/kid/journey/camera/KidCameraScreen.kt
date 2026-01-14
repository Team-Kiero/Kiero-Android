package com.kiero.presentation.kid.journey.camera

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidCameraRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    KidCameraScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
    )
}

@Composable
private fun KidCameraScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

}

@Composable
@Preview
private fun KidCameraScreenPreview() {
    KieroTheme {
        KidCameraScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
        )
    }
}