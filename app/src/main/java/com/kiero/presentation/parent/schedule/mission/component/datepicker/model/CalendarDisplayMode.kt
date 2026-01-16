package com.kiero.presentation.parent.schedule.mission.component.datepicker.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kiero.core.designsystem.theme.KieroTheme
import java.time.LocalDate

sealed interface CalendarDisplayMode {
    data class Normal(
        val procedureCountByDate: Map<LocalDate, Int>
    ) : CalendarDisplayMode
}

data class DateTextStyle(
    val dateState: DateState?,
    val isSelected: Boolean
) {
    val textColor: Color
        @Composable
        get() = when {
            isSelected && dateState == DateState.FUTURE -> KieroTheme.colors.black
            dateState == DateState.PAST -> KieroTheme.colors.gray700
            dateState == DateState.TODAY -> KieroTheme.colors.gray400
            dateState == DateState.FUTURE -> KieroTheme.colors.white
            else -> KieroTheme.colors.black
        }
}
