package com.kiero.presentation.parent.schedule.plan.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.kiero.presentation.parent.schedule.model.ScheduleEvent
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class DayList(
    val day: String,
)

enum class ColorType(val color: Color) {
    SCHEDULE1(Color(0xFFCFFFFA)),
    SCHEDULE2(Color(0xFFFFFEE9)),
    SCHEDULE3(Color(0xFFBFFFE3)),
    SCHEDULE4(Color(0xFF34D9D3)),
    SCHEDULE5(Color(0xFF7BBDFF)),
}


object ScheduleData {

    val fakeScheduleEvents = listOf(
        ScheduleEvent(
            id = "1",
            name = "학교",
            isRecurring = true,
            startTime = "08:00",
            endTime = "12:00",
            scheduleColor = "SCHEDULE4",
            dayOfWeek = "MON",
            date = null
        ),
        ScheduleEvent(
            id = "2",
            name = "수영",
            isRecurring = true,
            startTime = "08:00",
            endTime = "13:00",
            scheduleColor = "SCHEDULE4",
            dayOfWeek = "TUE",
            date = null
        ),
        ScheduleEvent(
            id = "3",
            name = "피아노",
            isRecurring = true,
            startTime = "08:00",
            endTime = "09:00",
            scheduleColor = "SCHEDULE3",
            dayOfWeek = "WED",
            date = null
        ),
        ScheduleEvent(
            id = "4",
            name = "영어",
            isRecurring = true,
            startTime = "08:00",
            endTime = "09:00",
            scheduleColor = "SCHEDULE2",
            dayOfWeek = "THU",
            date = null
        ),
        ScheduleEvent(
            id = "5",
            name = "미술",
            isRecurring = true,
            startTime = "08:00",
            endTime = "09:00",
            scheduleColor = "SCHEDULE1",
            dayOfWeek = "FRI",
            date = null
        ),
        ScheduleEvent(
            id = "6",
            name = "독서",
            isRecurring = true,
            startTime = "08:00",
            endTime = "09:00",
            scheduleColor = "SCHEDULE2",
            dayOfWeek = "SAT",
            date = null
        ),
        ScheduleEvent(
            id = "7",
            name = "운동",
            isRecurring = true,
            startTime = "08:00",
            endTime = "09:00",
            scheduleColor = "SCHEDULE5",
            dayOfWeek = "SUN",
            date = null
        ),
        ScheduleEvent(
            id = "8",
            name = "태권도",
            isRecurring = true,
            startTime = "14:00",
            endTime = "16:00",
            scheduleColor = "SCHEDULE3",
            dayOfWeek = "TUE",
            date = null
        )
    )

    val fakeShortScheduleEvents = listOf(
        ScheduleEvent(
            id = "1",
            name = "학교",
            isRecurring = true,
            startTime = "08:00",
            endTime = "12:00",
            scheduleColor = "SCHEDULE4",
            dayOfWeek = "MON",
            date = null
        ),
        ScheduleEvent(
            id = "2",
            name = "수영",
            isRecurring = true,
            startTime = "08:00",
            endTime = "13:00",
            scheduleColor = "SCHEDULE2",
            dayOfWeek = "TUE",
            date = null
        ),
        ScheduleEvent(
            id = "3",
            name = "피아노",
            isRecurring = true,
            startTime = "08:00",
            endTime = "09:00",
            scheduleColor = "SCHEDULE3",
            dayOfWeek = "WED",
            date = null
        ),
        ScheduleEvent(
            id = "4",
            name = "태권도",
            isRecurring = true,
            startTime = "14:00",
            endTime = "16:00",
            scheduleColor = "SCHEDULE2",
            dayOfWeek = "TUE",
            date = null
        )
    )

    val fakeDayList = persistentListOf(
        DayList("8(월)"),
        DayList("9(화)"),
        DayList("10(수)"),
        DayList("11(목)"),
        DayList("12(금)"),
        DayList("13(토)"),
        DayList("14(일)")
    )

    fun createSampleEvent(
        id: String = "sample",
        name: String = "샘플 일정",
        startTime: String = "09:00",
        endTime: String = "10:00",
        color: String = "SCHEDULE1",
        dayOfWeek: String = "MON",
    ) = ScheduleEvent(
        id = id,
        name = name,
        isRecurring = true,
        startTime = startTime,
        endTime = endTime,
        scheduleColor = color,
        dayOfWeek = dayOfWeek,
        date = null
    )
}