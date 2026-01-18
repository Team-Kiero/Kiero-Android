package com.kiero.presentation.parent.schedule.mission.component.datepicker.model

import java.time.LocalDate

fun getDateState(date: LocalDate, today: LocalDate = LocalDate.now()): DateState {
    return when {
        date.isBefore(today) -> DateState.PAST
        date.isEqual(today) -> DateState.TODAY
        else -> DateState.FUTURE
    }
}

