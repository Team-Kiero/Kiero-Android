package com.kiero.presentation.kid.journey.fire.state

import androidx.compose.runtime.Immutable

@Immutable
data class KidFireState(
    val date: String = "",
    val stones : Int = 0
)