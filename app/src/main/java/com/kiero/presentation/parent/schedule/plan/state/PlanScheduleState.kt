package com.kiero.presentation.parent.schedule.plan.state

import com.kiero.data.parent.plan.model.NormalScheduleModel
import com.kiero.data.parent.plan.model.PlanAllModel
import com.kiero.data.parent.plan.model.RecurringScheduleModel
import com.kiero.presentation.parent.schedule.model.ScheduleEvent
import com.kiero.presentation.signup.parent.model.ParentInfoUiModel
import java.time.DayOfWeek
import java.time.LocalDate

data class ParentScheduleState(
    val planAllModel: PlanAllModel? = null,
    val parentInfo: ParentInfoUiModel = ParentInfoUiModel(),
    val currentDate: LocalDate = LocalDate.now(),
    val isFetching: Boolean = false,
) {
    val dateRangeText: String
        get() {
            val monday = currentDate.with(DayOfWeek.MONDAY)
            val sunday = currentDate.with(DayOfWeek.SUNDAY)
            return "${monday.monthValue}월 ${((monday.dayOfMonth - 1) / 7) + 1}주차"
        }

    fun ScheduleEvent.getIndices(): List<Int> {
        return if (isRecurring) {
            this.dayOfWeek?.split(",")?.mapNotNull { day ->
                day.trim().toDayIndex()
            } ?: listOf(0)
        } else {
            try {
                val localDate = java.time.LocalDate.parse(this.date)
                listOf(localDate.dayOfWeek.value - 1)
            } catch (e: Exception) {
                listOf(0)
            }
        }
    }

    fun String.toDayIndex(): Int {
        return when (this.uppercase()) {
            "MON" -> 0
            "TUE" -> 1
            "WED" -> 2
            "THU" -> 3
            "FRI" -> 4
            "SAT" -> 5
            "SUN" -> 6
            else -> 0
        }
    }

}

fun RecurringScheduleModel.toScheduleEvent() = ScheduleEvent(
    id = "recurring_${name}_${startTime}",
    name = name,
    isRecurring = true,
    startTime = startTime,
    endTime = endTime,
    scheduleColor = colorCode,
    dayOfWeek = dayOfWeek,
    date = null
)

fun NormalScheduleModel.toScheduleEvent() = ScheduleEvent(
    id = "normal_${name}_${date}",
    name = name,
    isRecurring = false,
    startTime = startTime,
    endTime = endTime,
    scheduleColor = colorCode,
    dayOfWeek = null,
    date = date
)