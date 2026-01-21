package com.kiero.core.common.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter

val String.formattedDeadLine: String
    get() {
        if (this.isBlank()) return ""

        return try {
            val dateString = this.take(10)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dueDate = LocalDate.parse(dateString, formatter)
            val today = LocalDate.now()

            when {
                dueDate.isEqual(today) -> "오늘까지"
                else -> {
                    val outputFormatter = DateTimeFormatter.ofPattern("MM.dd")
                    dueDate.format(outputFormatter)
                }
            }
        } catch (e: Exception) {
            this
        }
    }


val String.toRelativeDayFromDate: String?
    get() {
        if (this.isBlank()) return null

        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("u-M-d")
            val dueDate = LocalDate.parse(this.take(10), inputFormatter)

            val today = LocalDate.now()
            val tomorrow = today.plusDays(1)

            when {
                dueDate.isEqual(today) -> "오늘"
                dueDate.isEqual(tomorrow) -> "내일"
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }


val String.formatWithDayOfWeek: String
    get() {
        if (this.isBlank()) return ""

        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("u-M-d")
            val date = LocalDate.parse(this, inputFormatter)

            val outputFormatter =
                DateTimeFormatter.ofPattern("yyyy.M.d.(E)", java.util.Locale.KOREAN)

            date.format(outputFormatter)
        } catch (e: Exception) {
            this
        }
    }

fun String.validateAsStartTime(): String {
    try {
        val parts = this.split(" ")
        if (parts.size != 2) return this

        val timeParts = parts[0].split(":")
        if (timeParts.size != 2) return this

        val hour = timeParts[0].toIntOrNull() ?: return this
        val minute = timeParts[1].toIntOrNull() ?: return this
        val amPm = parts[1]

        return when (amPm) {
            "AM" -> {
                if (hour < 8) {
                    "08:00 AM"
                } else {
                    this
                }
            }

            "PM" -> {
                if (hour > 10) {
                    "10:00 PM"
                } else {
                    this
                }
            }

            else -> this
        }
    } catch (e: Exception) {
        return this
    }
}