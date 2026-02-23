package com.kiero.presentation.parent.mission.component.datepicker.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import java.time.YearMonth

@Immutable
data class CalendarMonth(
    val yearMonth: YearMonth,
    val weekDays: ImmutableList<ImmutableList<CalendarDay>>
)