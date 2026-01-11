package com.kiero.core.trigger

import androidx.compose.runtime.staticCompositionLocalOf
import com.kiero.core.model.trigger.GlobalUiEventHolder

val LocalGlobalUiEventTrigger = staticCompositionLocalOf<GlobalUiEventHolder> {
    error("No GlobalUiEvent Trigger provided")
}
