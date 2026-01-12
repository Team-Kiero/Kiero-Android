package com.kiero.core.model.trigger

import androidx.compose.runtime.Immutable

@Immutable
data class SnackbarState(
    val message: String = "",
)
