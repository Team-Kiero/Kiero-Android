package com.kiero.presentation.parent.schedule.plan.component.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ScheduleWeekTopbar(
    currentDate: LocalDate,
    modifier: Modifier = Modifier,
) {
   val DAY_FORMATTER = DateTimeFormatter.ofPattern("d(E)", Locale.KOREAN)

    val weekDaysList = remember(currentDate) {
        val startOfWeek = currentDate.with(DayOfWeek.MONDAY)
        (0L..6L).map { offset ->
            startOfWeek.plusDays(offset).format(DAY_FORMATTER)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Unspecified)
            .padding(start = 28.dp, end = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        weekDaysList.forEachIndexed { index, day ->
            Text(
                text = day,
                color = if (index == 0) KieroTheme.colors.main else KieroTheme.colors.gray100,
                style = KieroTheme.typography.regular.body5,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun WeekTopbarPreview() {
    KieroTheme {
        ScheduleWeekTopbar(
            currentDate = LocalDate.of(2026, 1, 20)
        )
    }
}