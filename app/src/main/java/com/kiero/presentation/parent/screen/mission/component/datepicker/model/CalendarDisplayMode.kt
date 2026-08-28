package com.kiero.presentation.parent.screen.mission.component.datepicker.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.kiero.core.designsystem.theme.KieroTheme
import kotlinx.collections.immutable.ImmutableMap
import java.time.LocalDate

@Stable
sealed interface CalendarDisplayMode {
    @Immutable
    data class Normal(
        val procedureCountByDate: ImmutableMap<LocalDate, Int>
    ) : CalendarDisplayMode
}

data class DateTextStyle(
    val dateState: DateState?,
    val isSelected: Boolean
) {
    val textColor: Color
        @Composable
        get() = when {
            isSelected -> KieroTheme.colors.black
            isSelected && dateState == DateState.FUTURE -> KieroTheme.colors.black
            dateState == DateState.PAST -> KieroTheme.colors.gray700
            dateState == DateState.TODAY -> KieroTheme.colors.gray400
            dateState == DateState.FUTURE -> KieroTheme.colors.white
            else -> KieroTheme.colors.black
        }
}
