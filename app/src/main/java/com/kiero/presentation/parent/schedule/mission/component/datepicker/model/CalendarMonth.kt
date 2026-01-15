package com.kiero.presentation.parent.schedule.mission.component.datepicker.model

import androidx.compose.runtime.Immutable
import java.time.YearMonth

@Immutable
data class CalendarMonth(
    val yearMonth: YearMonth,
    val weekDays: List<List<CalendarDay>>
)