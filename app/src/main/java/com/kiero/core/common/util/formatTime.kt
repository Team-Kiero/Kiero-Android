package com.kiero.core.common.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


object ParentFormatters{
    val SERVER_TIME_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val UI_TIME_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)
    val DAY_OF_WEEK_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("d(E)", Locale.KOREAN)
    val DATE_RANGE_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("M.d(E)", Locale.KOREAN)
    private val DATE_WITH_DAY_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.M.d.(E)", Locale.KOREAN)


    fun formatDuration(totalSeconds: Int, includeHours: Boolean = false): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (includeHours || hours > 0) {
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }
    /*
    * 부모 - 일정, 미션에 공통적으로 사용되는 포맷팅 입니다.
    * 형식은 yyyy-mm-dd -> yyyy.M.d.(E) 로 변환되는 형식입니다.
    */

    fun formatDateWithDayOfWeek(dateStr: String): String {
        if (dateStr.isBlank()) return ""
        return try {
            val date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("u-M-d"))
            date.format(DATE_WITH_DAY_FMT)
        } catch (e: Exception) {
            dateStr
        }
    }
}
fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}