package com.kiero.presentation.parent.schedule.mission.component.datepicker.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.CalendarDay
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.DateState
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.DateTextStyle
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.getDateState
import java.time.LocalDate


@Composable
fun DayItem(
    day: CalendarDay,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val today = LocalDate.now()
    val dateState = if (day is CalendarDay.Date) {
        remember(day.date, today) { getDateState(day.date, today) }
    } else {
        null
    }
    val isClickable = dateState == DateState.FUTURE

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .noRippleClickable(
                onClick = {
                    if (day is CalendarDay.Date && isClickable) {
                        onDateClick(day.date)
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        when (day) {
            CalendarDay.Empty -> {}

            is CalendarDay.Date.Normal -> NormalDateContent(
                day = day,
                isSelected = isSelected,
                dateState = dateState!!
            )
        }
    }
}

@Composable
private fun NormalDateContent(
    day: CalendarDay.Date.Normal,
    dateState: DateState,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val backgroundColor = when {
        isSelected && dateState == DateState.FUTURE -> KieroTheme.colors.main
        dateState == DateState.TODAY -> KieroTheme.colors.gray800
        else -> Color.Transparent
    }

    val borderColor = when {
        isSelected && dateState == DateState.FUTURE -> KieroTheme.colors.main
        dateState == DateState.TODAY -> KieroTheme.colors.gray800
        else -> Color.Transparent
    }

    val showBorder = (isSelected && dateState == DateState.FUTURE) || dateState == DateState.TODAY

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape = CircleShape)
            .background(color = backgroundColor)
            .then(
                if (showBorder) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        DateText(
            dayOfMonth = day.date.dayOfMonth,
            dateState = dateState,
            isSelected = isSelected
        )
    }
}

@Composable
private fun DateText(
    dayOfMonth: Int,
    dateState: DateState? = null,
    isSelected: Boolean = false,
) {
    val style = remember(dateState, isSelected) {
        DateTextStyle(dateState, isSelected)
    }

    Text(
        text = dayOfMonth.toString(),
        color = style.textColor,
        style = KieroTheme.typography.semiBold.title4
    )
}


@Preview(showBackground = true)
@Composable
private fun DayItemPreview() {
    KieroTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            val today = LocalDate.now()

            DayItem(
                day = CalendarDay.Date.Normal(today.minusDays(2), procedureCount = 0),
                onDateClick = {},
                modifier = Modifier.size(48.dp)
            )

            DayItem(
                day = CalendarDay.Date.Normal(today, procedureCount = 0),
                onDateClick = {},
                modifier = Modifier.size(48.dp)
            )

            DayItem(
                day = CalendarDay.Date.Normal(today.plusDays(1), procedureCount = 0),
                onDateClick = {},
                modifier = Modifier.size(48.dp)
            )

            DayItem(
                day = CalendarDay.Date.Normal(today.plusDays(2), procedureCount = 0),
                isSelected = true,
                onDateClick = {},
                modifier = Modifier.size(48.dp)
            )
        }
    }
}