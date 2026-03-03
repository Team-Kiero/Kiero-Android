package com.kiero.presentation.kid.journey.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
fun KidMapRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    KidMapScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp
    )
}

@Composable
private fun KidMapScreen (
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {

}