package com.kiero.presentation.parent.schedule.mission.component.datepicker.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.CalendarDay
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.CalendarDisplayMode
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.CalendarMonth
import com.kiero.presentation.parent.schedule.mission.component.datepicker.util.daysOfWeek
import com.kiero.presentation.parent.schedule.mission.component.datepicker.util.generateMonthData
import kotlinx.collections.immutable.toImmutableList
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun BasicCalendar(
    displayMode: CalendarDisplayMode,
    modifier: Modifier = Modifier,
    yearMonth: YearMonth = YearMonth.now(),
    firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY,
    dayContent: @Composable (CalendarDay) -> Unit,
) {
    val monthData = remember(yearMonth, displayMode, firstDayOfWeek) {
        generateMonthData(
            yearMonth = yearMonth,
            firstDayOfWeek = firstDayOfWeek,
            displayMode = displayMode
        ).calendarMonth
    }

    val daysOfWeek = remember(firstDayOfWeek) {
        daysOfWeek(firstDayOfWeek = firstDayOfWeek).toImmutableList()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = KieroTheme.colors.gray900)
    ) {
        DaysOfWeekTitle(
            daysOfWeek = daysOfWeek
        )

        CalendarMonthGrid(
            month = monthData,
            dayContent = dayContent
        )
    }
}

@Composable
private fun CalendarMonthGrid(
    month: CalendarMonth,
    modifier: Modifier = Modifier,
    dayContent: @Composable (CalendarDay) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (week in month.weekDays) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (day in week) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    ) {
                        dayContent(day)
                    }
                }
            }
        }
    }
}

