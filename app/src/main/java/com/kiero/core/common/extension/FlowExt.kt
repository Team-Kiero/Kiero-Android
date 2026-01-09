package com.kiero.core.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> Flow<T>.collectSideEffect(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: suspend (T) -> Unit
) {
    val lifecycle = lifecycleOwner.lifecycle

    LaunchedEffect(this, lifecycle) {
        flowWithLifecycle(lifecycle, minActiveState)
            .collect(action)
    }
}

@Composable
fun <T> Flow<T>.collectLatestSideEffect(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    throttleTime: Long = 500L,
    action: suspend (T) -> Unit
) {
    val lifecycle = lifecycleOwner.lifecycle
    var lastEmitTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(this, lifecycle) {
        flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect { value ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmitTime > throttleTime) {
                lastEmitTime = currentTime
                action(value)
            }
        }
    }
}
