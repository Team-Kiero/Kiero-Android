package com.kiero.presentation.parent.schedule.mission.component.datepicker.util

import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.CalendarDay
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.CalendarDisplayMode
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.CalendarMonth
import kotlinx.collections.immutable.toImmutableList
import java.time.DayOfWeek
import java.time.YearMonth

data class MonthData(
    private val month: YearMonth,
    private val inDays: Int,
    private val displayMode: CalendarDisplayMode,
) {
    private val monthLength = month.lengthOfMonth()
    private val totalDays = inDays + monthLength
    private val totalWeeks = (totalDays + 6) / 7
    private val totalCells = totalWeeks * 7
    private val firstDay = month.atStartOfMonth()
    private val rows = (0 until totalCells).chunked(7)

    val calendarMonth: CalendarMonth = CalendarMonth(
        month,
        rows.map { row ->
            row.map { dayOffset -> getDay(dayOffset) }.toImmutableList()
        }.toImmutableList()
    )

    private fun getDay(dayOffset: Int): CalendarDay {
        if (dayOffset < inDays || dayOffset >= inDays + monthLength) {
            return CalendarDay.Empty
        }

        val date = firstDay.plusDays((dayOffset - inDays).toLong())

        return when (displayMode) {
            is CalendarDisplayMode.Normal -> {
                val count = displayMode.procedureCountByDate[date] ?: 0
                CalendarDay.Date.Normal(date = date, procedureCount = count)
            }
        }
    }
}

fun generateMonthData(
    yearMonth: YearMonth,
    firstDayOfWeek: DayOfWeek,
    displayMode: CalendarDisplayMode,
): MonthData {
    val firstDay = yearMonth.atStartOfMonth()
    val inDays = firstDayOfWeek.daysUntil(firstDay.dayOfWeek)
    return MonthData(yearMonth, inDays, displayMode)
}