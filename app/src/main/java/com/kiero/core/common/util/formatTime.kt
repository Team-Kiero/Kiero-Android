package com.kiero.core.common.util

import java.time.format.DateTimeFormatter
import java.util.Locale


object PlanFormatters{
    val SERVER_TIME_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val UI_TIME_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)
    val DAY_OF_WEEK_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("d(E)", Locale.KOREAN)
    val DATE_RANGE_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("M.d(E)", Locale.KOREAN)


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
    // TODO : 필요시 Util에 넣어서 사용하기
}
fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}