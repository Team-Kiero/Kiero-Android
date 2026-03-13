package com.kiero.core.model.trigger

import androidx.compose.runtime.Stable
import com.kiero.core.navigation.Route
import kotlinx.coroutines.flow.SharedFlow

@Stable
class GlobalUiEventHolder(
    val dialogTrigger: DialogTrigger,
    val showToast: (String) -> Unit,
    val showSnackbar: (SnackbarState) -> Unit,
    val tabReselectedEvent: SharedFlow<Route>,
    val onTabReselected: (Route) -> Unit
)
