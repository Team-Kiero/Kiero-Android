package com.kiero.presentation.parent.screen.mission.component.datepicker.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.mission.component.datepicker.model.CalendarDay
import com.kiero.presentation.parent.screen.mission.component.datepicker.model.CalendarDisplayMode
import com.kiero.presentation.parent.screen.mission.component.datepicker.util.nextMonth
import com.kiero.presentation.parent.screen.mission.component.datepicker.util.previousMonth
import kotlinx.collections.immutable.persistentMapOf
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun KieroCalendar(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    displayMode: CalendarDisplayMode,
    onDateClick: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CalendarMonthHeader(
            onLeftArrowClick = { onMonthChange(yearMonth.previousMonth) },
            onRightArrowClick = { onMonthChange(yearMonth.nextMonth) },
            yearMonth = yearMonth
        )

        BasicCalendar(
            yearMonth = yearMonth,
            displayMode = displayMode,
            dayContent = { day ->
                DayItem(
                    day = day,
                    isSelected = day is CalendarDay.Date && day.date == selectedDate,
                    onDateClick = onDateClick
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KieroCalendarPreview() {
    KieroTheme {
        var yearMonth by remember { mutableStateOf(YearMonth.now()) }
        var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
        val today = LocalDate.now()

        KieroCalendar(
            yearMonth = yearMonth,
            selectedDate = selectedDate,
            displayMode = CalendarDisplayMode.Normal(
                procedureCountByDate = persistentMapOf(
                    today.minusDays(2) to 3,
                    today to 1,
                    today.plusDays(10) to 2
                )
            ),
            onDateClick = { selectedDate = it },
            onMonthChange = { yearMonth = it }
        )
    }
}
