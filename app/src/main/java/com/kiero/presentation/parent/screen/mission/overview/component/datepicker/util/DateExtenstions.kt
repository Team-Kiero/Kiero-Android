package com.kiero.presentation.parent.screen.mission.overview.component.datepicker.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

fun daysOfWeek(firstDayOfWeek: DayOfWeek = firstDayOfWeekFromLocale()): List<DayOfWeek> {
    val pivot = 7 - firstDayOfWeek.ordinal
    val daysOfWeek = DayOfWeek.entries
    return daysOfWeek.takeLast(pivot) + daysOfWeek.dropLast(pivot)
}

fun firstDayOfWeekFromLocale(locale: Locale = Locale.getDefault()): DayOfWeek =
    WeekFields.of(locale).firstDayOfWeek

fun YearMonth.atStartOfMonth(): LocalDate = this.atDay(1)

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.from(this)

val YearMonth.nextMonth: YearMonth
    get() = this.plusMonths(1)

val YearMonth.previousMonth: YearMonth
    get() = this.minusMonths(1)