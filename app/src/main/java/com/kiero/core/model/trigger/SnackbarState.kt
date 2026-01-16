package com.kiero.core.model.trigger

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class SnackbarState(
    val message: String = "",
    val bottomPadding: Dp = 0.dp,
)
