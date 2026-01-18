package com.kiero.core.common.extension

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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

val String.formattedAlarmDate: String
    get() {
        if (this.isBlank()) return ""
        return try {
            val date = if (this.contains("T")) LocalDateTime.parse(this).toLocalDate()
            else LocalDate.parse(this)
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)
            "${date.format(formatter)}.($dayOfWeek)"
        } catch (e: Exception) {
            this
        }
    }

val String.formattedAlarmTime: String
    get() {
        if (this.isBlank() || !this.contains("T")) return "00 : 00"
        return try {
            LocalDateTime.parse(this).format(DateTimeFormatter.ofPattern("HH : mm"))
        } catch (e: Exception) {
            "00 : 00"
        }
    }

fun String.toHighlightedText(
    highlightColor: Color,
    vararg highlightTexts: String?
): AnnotatedString = buildAnnotatedString {
    append(this@toHighlightedText)
    highlightTexts.forEach { target ->
        if (target.isNullOrBlank()) return@forEach

        var startIndex = indexOf(target)
        while (startIndex >= 0) {
            addStyle(
                style = SpanStyle(color = highlightColor),
                start = startIndex,
                end = startIndex + target.length
            )
            startIndex = indexOf(target, startIndex + target.length)
        }
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