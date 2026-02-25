package com.kiero.presentation.parent.screen.schedule.model

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
){
    companion object {
        fun empty(): List<ScheduleEvent> = emptyList()

        fun mock() = listOf(
            ScheduleEvent(
                id = "1",
                name = "태권",
                isRecurring = true,
                startTime = "09:00",
                endTime = "10:20",
                scheduleColor = "#4A5F7A",
                dayOfWeek = "MON,WED,FRI",
                date = null
            ),
            ScheduleEvent(
                id = "2",
                name = "돌봄교실",
                isRecurring = false,
                startTime = "14:00",
                endTime = "15:00",
                scheduleColor = "#E91E63",
                dayOfWeek = "TUE",
                date = null
            ),
            ScheduleEvent(
                id = "3",
                name = "임상헌생일파티",
                isRecurring = false,
                startTime = "11:00",
                endTime = "12:30",
                scheduleColor = "#FF9800",
                dayOfWeek = "SUN",
                date = "2024-03-20"
            )
        )
    }
}

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