package com.kiero.presentation.parent.schedule.plan.state

import androidx.compose.runtime.Immutable
import com.kiero.data.parent.plan.model.NormalScheduleModel
import com.kiero.data.parent.plan.model.PlanAllModel
import com.kiero.data.parent.plan.model.RecurringScheduleModel
import com.kiero.presentation.parent.schedule.model.ScheduleEvent
import com.kiero.presentation.signup.parent.model.ParentInfoUiModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

@Immutable
data class ParentScheduleState(
    val planAllModel: PlanAllModel? = null,
    val parentInfo: ParentInfoUiModel = ParentInfoUiModel(),
    val currentDate: LocalDate = LocalDate.now(),
    val isFetching: Boolean = false,
    val isFireLit : Boolean = false,
    val isLogoutDialogVisible: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
) {
    val canGoNext: Boolean
        get() = ChronoUnit.WEEKS.between(LocalDate.now(), currentDate) < 12

    val canGoPrevious: Boolean
        get() = ChronoUnit.WEEKS.between(LocalDate.now(), currentDate) > -12
    val dateRangeText: String
        get() {
            val thursday = currentDate.with(DayOfWeek.THURSDAY)
            val monthValue = thursday.monthValue

            val firstThursday = thursday.with(TemporalAdjusters.firstDayOfMonth())
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY))

            val lastThursday = thursday.with(TemporalAdjusters.lastDayOfMonth())
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY))

            val weekNum = ChronoUnit.WEEKS.between(firstThursday, thursday).toInt() + 1

            return when {
                thursday == lastThursday -> "${monthValue}월 마지막 주차"
                else -> "${monthValue}월 ${weekNum}주차"
            }
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

fun RecurringScheduleModel.toUiModel() = ScheduleEvent(
    id = "recurring_${name}_${startTime}",
    name = name,
    isRecurring = true,
    startTime = startTime,
    endTime = endTime,
    scheduleColor = colorCode,
    dayOfWeek = dayOfWeek,
    date = null
)

fun NormalScheduleModel.toUiModel() = ScheduleEvent(
    id = "normal_${name}_${date}",
    name = name,
    isRecurring = false,
    startTime = startTime,
    endTime = endTime,
    scheduleColor = colorCode,
    dayOfWeek = null,
    date = date
)