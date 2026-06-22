package com.kiero.presentation.parent.screen.mission.component.datepicker.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.disableUpWardEvent
import com.kiero.core.designsystem.component.bottomsheet.KieroBottomSheet
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.mission.component.datepicker.model.CalendarDisplayMode
import com.kiero.presentation.parent.screen.schedule.plan.component.picker.PickerTopbar
import kotlinx.collections.immutable.persistentMapOf
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    initialDate: LocalDate = LocalDate.now(),
) {
    var yearMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(initialDate) }

    KieroBottomSheet (
        onDismiss = {
            selectedDate?.let { date -> onDateSelected(date) }
            onDismissRequest()
        },
        dragHandle = null,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .disableUpWardEvent()
                .padding(vertical = 20.dp, horizontal = 30.dp)
        ) {
            PickerTopbar(
                title = "마감일",
                leftIconRes = R.drawable.ic_close_light,
                leftIconClick = {
                    selectedDate?.let { date -> onDateSelected(date) }
                    onDismissRequest()
                },
                rightIconRes = R.drawable.ic_check,
                rightIconClick = {
                    selectedDate?.let { date -> onDateSelected(date) }
                    onDismissRequest()
                }
            )

            KieroCalendar(
                yearMonth = yearMonth,
                selectedDate = selectedDate,
                displayMode = CalendarDisplayMode.Normal(
                    procedureCountByDate = persistentMapOf()
                ),
                onDateClick = { selectedDate = it },
                onMonthChange = { yearMonth = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )

        }
    }
}

@Preview
@Composable
private fun PreviewCalendarBottomSheet() {
    KieroTheme {
        CalendarBottomSheet(
            initialDate = LocalDate.now(),
            onDismissRequest = {},
            onDateSelected = {}
        )
    }
}
