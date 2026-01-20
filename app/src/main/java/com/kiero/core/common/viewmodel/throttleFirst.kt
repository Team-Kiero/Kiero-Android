
package com.kiero.core.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun ViewModel.throttleFirst(
    duration: Long = 500L,
    action: suspend () -> Unit
): Job {
    return viewModelScope.launch {
        action()
        delay(duration)
    }
}