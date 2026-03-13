package com.kiero.core.designsystem.component.dialog.action

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
fun interface DialogAction {
    @Composable
    operator fun invoke(modifier: Modifier)
}
