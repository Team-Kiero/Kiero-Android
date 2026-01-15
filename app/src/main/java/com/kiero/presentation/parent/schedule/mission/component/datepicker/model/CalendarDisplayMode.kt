package com.kiero.presentation.parent.schedule.mission.component.datepicker.model

import java.time.LocalDate

sealed interface CalendarDisplayMode {
    data class Normal(
        val procedureCountByDate: Map<LocalDate, Int>
    ) : CalendarDisplayMode
}