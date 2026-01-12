package com.kiero.core.designsystem.component.chip.action

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun interface ChipAction {
    @Composable
    operator fun invoke(modifier: Modifier)
}