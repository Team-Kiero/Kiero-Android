package com.kiero.core.common.util

import com.kiero.core.model.UiState

val <T> UiState<T>.successData: T?
    get() = (this as? UiState.Success)?.data