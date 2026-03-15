package com.kiero.presentation.parent.screen.schedule.plan.state

import androidx.compose.runtime.Immutable
import com.kiero.data.parent.plan.model.PlanAllModel
import com.kiero.presentation.parent.screen.schedule.model.ScheduleEvent
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
    val isFireLit: Boolean = false,
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
            val targetMonth = currentDate.monthValue

            val firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth())

            val firstMondayOfTargetMonthWeek =
                firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

            val currentMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

            val weekNum =
                ChronoUnit.WEEKS.between(firstMondayOfTargetMonthWeek, currentMonday).toInt() + 1

            return "${targetMonth}월 ${weekNum}주차"
        }

    val navInitialDate: String
        get() {
            val today = LocalDate.now()
            val currentMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val viewedMonday = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

            return if (viewedMonday.isBefore(currentMonday)) {
                today.toString()
            } else {
                currentDate.toString()
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

    companion object {
        fun formatRepeatText(dayOfWeek: String): String {
            val dayMap = linkedMapOf(
                "MON" to "월", "TUE" to "화", "WED" to "수",
                "THU" to "목", "FRI" to "금", "SAT" to "토", "SUN" to "일"
            )
            val days = dayOfWeek.split(",").map { it.trim() }
            if (days.containsAll(dayMap.keys)) return "매일 반복"
            val sorted = dayMap.keys.filter { it in days }.mapNotNull { dayMap[it] }.joinToString("")
            return "매주 ${sorted} 반복"
        }
    }
}

sealed interface ParentScheduleSideEffect {
    data class ShowSnackBar(val message: String) : ParentScheduleSideEffect
    data object navigateUp : ParentScheduleSideEffect
    data object ShowDialog : ParentScheduleSideEffect
}

