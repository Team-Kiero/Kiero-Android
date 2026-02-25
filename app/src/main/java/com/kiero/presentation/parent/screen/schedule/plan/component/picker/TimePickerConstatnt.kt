package com.kiero.presentation.parent.screen.schedule.plan.component.picker

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object TimePickerConstants {
    val hours: ImmutableList<String> =
        (listOf("", "") + (1..12).map { it.toString() } + listOf("", "")).toImmutableList()

    val minutes: ImmutableList<String> =
        (listOf("", "") + (0..59).map { it.toString().padStart(2, '0') } + listOf(
            "",
            ""
        )).toImmutableList()

    val amPmList: ImmutableList<String> =
        (listOf("", "") + listOf("AM", "PM") + listOf("", "")).toImmutableList()
}