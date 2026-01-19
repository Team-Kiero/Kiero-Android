package com.kiero.presentation.parent.schedule.mission.component.datepicker.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.component.datepicker.model.CalendarDisplayMode
import com.kiero.presentation.parent.schedule.plan.component.picker.PickerTopbar
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    var yearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
    val today = LocalDate.now()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = KieroTheme.colors.gray900,
        dragHandle = null,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 30.dp)
                .padding(bottom = 32.dp)
        ) {
            PickerTopbar(
                title = "마감일",
                leftIconRes = R.drawable.ic_close_light,
                leftIconClick = onDismissRequest,
                rightIconRes = R.drawable.ic_check,
                rightIconClick = {
                    selectedDate?.let { date ->
                        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        onDateSelected(formattedDate)
                    }
                }
            )

            KieroCalendar(
                yearMonth = yearMonth,
                selectedDate = selectedDate,
                displayMode = CalendarDisplayMode.Normal(
                    procedureCountByDate = emptyMap()
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
            onDismissRequest = {},
            onDateSelected = {}
        )
    }
}