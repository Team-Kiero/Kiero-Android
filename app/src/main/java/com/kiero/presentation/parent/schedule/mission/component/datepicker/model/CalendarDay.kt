package com.kiero.presentation.parent.schedule.mission.component.datepicker.model
import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
sealed interface CalendarDay {

    @Immutable
    data object Empty : CalendarDay

    @Immutable
    sealed interface Date : CalendarDay {
        val date: LocalDate

        @Immutable
        data class Normal(
            override val date: LocalDate,
            val procedureCount: Int = 0
        ) : Date
    }
}