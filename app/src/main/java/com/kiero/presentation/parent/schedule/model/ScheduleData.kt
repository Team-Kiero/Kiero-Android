package com.kiero.presentation.parent.schedule.model

import androidx.compose.ui.graphics.Color

data class ScheduleEvent(
    val id: String,
    val name: String,
    val isRecurring: Boolean,
    val startTime: String,
    val endTime: String,
    val scheduleColor: String,
    val dayOfWeek: String?,
    val date: String?,
)

data class ScheduleBlock(
    val id: String,
    val title: String,
    val color: Color,
    val startHour: Int,
    val startMinute: Int,
    val durationInSlots: Int,
    val dayIndex: Int,
    val blockPosition: BlockPosition
)