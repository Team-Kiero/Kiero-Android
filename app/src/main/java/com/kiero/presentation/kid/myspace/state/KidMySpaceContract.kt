package com.kiero.presentation.kid.myspace.state

import androidx.compose.runtime.Immutable

@Immutable
data class KidMySpaceState(
    val isNotificationChecked: Boolean = false,
    val showNotificationDialog: Boolean = false
)