package com.kiero.presentation.parent.screen.schedule.plan.component.picker

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object TimePickerConstants {
    val hours: ImmutableList<String> =
        (1..12).map { it.toString().padStart(2, '0') }.toImmutableList()

    val minutes: ImmutableList<String> =
        (0..59).map { it.toString().padStart(2, '0') }.toImmutableList()

    val amPmList: ImmutableList<String> =
        listOf("AM", "PM").toImmutableList()
}