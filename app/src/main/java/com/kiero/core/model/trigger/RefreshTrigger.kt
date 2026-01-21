package com.kiero.core.model.trigger

import androidx.compose.runtime.Stable
import com.kiero.presentation.main.navigation.component.BottomBarTab
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Stable
class RefreshState {
    private val _refreshEvent = MutableSharedFlow<BottomBarTab>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val refreshEvent: SharedFlow<BottomBarTab> = _refreshEvent.asSharedFlow()

    suspend fun trigger(tab: BottomBarTab) {
        _refreshEvent.emit(tab)
    }
}