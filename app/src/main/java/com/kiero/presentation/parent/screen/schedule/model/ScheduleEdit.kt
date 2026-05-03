package com.kiero.presentation.parent.screen.schedule.model

data class RecurringEditOverride(
    val originalScheduleId: Long,
    val effectiveFromDate: String,
    val originalDayOfWeek: String?,
)

object RecurringEditOverrideStore {
    var pending: RecurringEditOverride? = null
}