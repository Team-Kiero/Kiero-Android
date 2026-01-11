package com.kiero.core.designsystem.component.dialog.action

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun interface DialogAction {
    @Composable
    operator fun invoke(modifier: Modifier)
}