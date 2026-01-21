package com.kiero.presentation.parent.schedule.plan.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.parent.schedule.plan.model.ColorType
import com.kiero.presentation.parent.schedule.plan.model.TimeValidationResult
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Immutable
data class ParentPlanState(
    val selectedColorType: ColorType = ColorType.SCHEDULE1,
    val showColorPicker: Boolean = false,
    val isRecurring: Boolean = false,
    val selectedDays: Set<Int> = emptySet(),
    val startTime: String = "12:00 PM",
    val endTime: String = "12:00 PM",
    val selectedDate: String = LocalDate.now().toString(),
    val currentReferenceDate: LocalDate = LocalDate.now(),
    val isFireLit: Boolean = false,
    val isLoading: Boolean = false,
    val isLogoutDialogVisible: Boolean = false,
) {

    val formattedDays: String?
        get() = if (isRecurring && selectedDays.isNotEmpty()) {
            selectedDays.sorted().joinToString(", ") {
                when (it) {
                    0 -> "MON"
                    1 -> "TUE"
                    2 -> "WED"
                    3 -> "THU"
                    4 -> "FRI"
                    5 -> "SAT"
                    else -> "SUN"
                }
            }
        } else null

    val isTimeValid: Boolean
        get() = timeToMinutes(endTime) > timeToMinutes(startTime)

    fun formatTimeForServer(time: String): String {
        return try {
            if (time.isEmpty()) return "00:00:00"
            val parts = time.split(" ", ":")
            if (parts.size < 3) return "00:00:00"

            var hour = parts[0].toIntOrNull() ?: 0
            val minute = parts[1]
            val amPm = parts[2]

            when {
                amPm == "PM" && hour < 12 -> hour += 12
                amPm == "AM" && hour == 12 -> hour = 0
            }
            String.format("%02d:%s:00", hour, minute)
        } catch (e: Exception) {
            "00:00:00"
        }
    }


    val dateRangeText: String
        get() {
            val monday =
                currentReferenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val sunday = monday.plusDays(6)

            val formatter = DateTimeFormatter.ofPattern("M.d(E)", Locale.KOREAN)
            return "${monday.format(formatter)} - ${sunday.format(formatter)}"
        }


    val canGoToPrevious: Boolean
        get() {
            val today = LocalDate.now()
            val startOfThisWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val startOfCurrentViewWeek =
                currentReferenceDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            return startOfCurrentViewWeek.isAfter(startOfThisWeek)
        }

    val weekDaysList: List<String>
        get() {
            val monday = currentReferenceDate.with(DayOfWeek.MONDAY)
            val formatter =
                DateTimeFormatter.ofPattern("d(E)", Locale.KOREAN)
            return (0..6).map { offset ->
                monday.plusDays(offset.toLong()).format(formatter)
            }
        }

    private fun getWeekOfMonth(date: LocalDate): Int {
        return ((date.dayOfMonth - 1) / 7) + 1
    }


    fun isTimeValid(start: String, end: String): Boolean {
        val startVal = timeToMinutes(start)
        val endVal = timeToMinutes(end)
        return endVal > startVal
    }

    fun timeToMinutes(time: String): Int {
        return try {
            val parts = time.split(" ", ":")
            var hour = parts[0].toInt()
            val minute = parts[1].toInt()
            val amPm = parts[2]
            if (amPm == "PM" && hour < 12) hour += 12
            if (amPm == "AM" && hour == 12) hour = 0
            (hour * 60) + minute
        } catch (e: Exception) {
            0
        }
    }
}

sealed interface ParentPlanSideEffect {
    data class ShowSnackBar(val message: String) : ParentPlanSideEffect
    data object navigateUp : ParentPlanSideEffect
}
fun validateAndTimeAdjustment(timeStr: String): TimeValidationResult {
    return try {
        val regex = Regex("""(\d{1,2}):(\d{2})\s*(AM|PM)""", RegexOption.IGNORE_CASE)
        val match = regex.find(timeStr) ?: return TimeValidationResult(false, "잘못된 형식", timeStr)

        val (hStr, mStr, amPm) = match.destructured
        var hour = hStr.toInt()
        val minute = mStr.toInt()

        if (amPm.uppercase() == "PM" && hour != 12) hour += 12
        if (amPm.uppercase() == "AM" && hour == 12) hour = 0

        val totalMinutes = hour * 60 + minute
        val startLimit = 8 * 60
        val endLimit = 22 * 60

        when {
            totalMinutes < startLimit -> {
                TimeValidationResult(false, "시각은 08:00AM부터 설정가능합니다.", "08:00 AM")
            }

            totalMinutes > endLimit -> {
                TimeValidationResult(false, "시각은 10:00PM까지 설정가능합니다.", "10:00 PM")
            }

            else -> {
                TimeValidationResult(true, null, timeStr)
            }
        }
    } catch (e: Exception) {
        TimeValidationResult(false, "시간 확인 중 오류 발생", timeStr)
    }
}

fun parseLocalTime(timeStr: String): java.time.LocalTime {
    return try {
        val regex = Regex("""(\d{1,2}):(\d{2})\s*(AM|PM)""", RegexOption.IGNORE_CASE)
        val match = regex.find(timeStr) ?: return java.time.LocalTime.MIN
        val (hStr, mStr, amPm) = match.destructured
        var hour = hStr.toInt()
        val minute = mStr.toInt()
        if (amPm.uppercase() == "PM" && hour != 12) hour += 12
        if (amPm.uppercase() == "AM" && hour == 12) hour = 0
        java.time.LocalTime.of(hour, minute)
    } catch (e: Exception) {
        java.time.LocalTime.MIN
    }
}