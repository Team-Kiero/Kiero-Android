package com.kiero.core.model.trigger

import androidx.compose.runtime.Stable

@Stable
class GlobalUiEventHolder(
    val dialogTrigger: DialogTrigger,
    val showToast: (String) -> Unit,
    val showSnackbar: (SnackbarState) -> Unit
)
