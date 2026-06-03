package com.kiero.core.common.extension

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val DATE_FORMAT_DASH = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val DATE_FORMAT_DOT = DateTimeFormatter.ofPattern("MM.dd")
private val DATE_FORMAT_SINGLE_DIGIT = DateTimeFormatter.ofPattern("u-M-d")
private val DATE_FORMAT_WITH_DAY = DateTimeFormatter.ofPattern("yyyy.M.d.(E)", Locale.KOREAN)
private val SERVER_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
private val ALARM_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd")
private val ALARM_TIME_FORMAT = DateTimeFormatter.ofPattern("HH : mm")
private val KOREAN_TIME_FORMATTER = DateTimeFormatter.ofPattern("a hh : mm", Locale.KOREAN)
val ALARM_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
private val WISH_ARCHIVE_DATE_FMT = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)

fun String.formattedDeadLine(): String {
    if (this.isBlank()) return ""

    return try {
        val dateString = this.take(10)
        val dueDate = LocalDate.parse(dateString, DATE_FORMAT_DASH)
        val today = LocalDate.now()

        when {
            dueDate.isEqual(today) -> "오늘까지"
            else -> dueDate.format(DATE_FORMAT_DOT)
        }
    } catch (e: Exception) {
        this
    }
}

fun String.toRelativeDayFromDate(): String? {
    if (this.isBlank()) return null

    return try {
        val dueDate = LocalDate.parse(this.take(10), DATE_FORMAT_SINGLE_DIGIT)

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

fun String.formatWithDayOfWeek(): String {
    if (this.isBlank()) return ""

    return try {
        val date = LocalDate.parse(this, DATE_FORMAT_SINGLE_DIGIT)
        date.format(DATE_FORMAT_WITH_DAY)
    } catch (e: Exception) {
        this
    }
}

fun String.formattedAlarmDate(): String {
    if (this.isBlank()) return ""
    return try {
        val dateTime = LocalDateTime.parse(this, SERVER_DATE_FORMAT)
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)
        "${dateTime.format(ALARM_DATE_FORMAT)}.($dayOfWeek)"
    } catch (e: Exception) {
        this
    }
}

fun String.formattedAlarmTime(): String {
    if (this.isBlank()) return "00 : 00"
    return try {
        val dateTime = LocalDateTime.parse(this, SERVER_DATE_FORMAT)
        dateTime.format(ALARM_TIME_FORMAT)
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

 // 아이 이름 뒤 '이/가' 조사 자동 처리
fun String.withJosa(josaPair: String): String {
    if (this.isBlank()) return this
    if (josaPair.length != 2) return this

    val lastChar = this.last()
    if (lastChar < '가' || lastChar > '힣') {
        return this + josaPair[1]
    }

    val hasBatchim = (lastChar - 0xAC00.toChar()) % 28 > 0

    val particle = if (hasBatchim) {
        josaPair[0]
    } else {
        josaPair[1]
    }

    return this + particle
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

fun String.toShortTime(): String = split(":").take(2).joinToString(":")

fun String.toKoreanTimeString(): String {
    if (this.isBlank()) return this

    return try {
        val time = LocalTime.parse(this)
        time.format(KOREAN_TIME_FORMATTER)
    } catch (e: Exception) {
        this
    }
}

fun String.toWishArchiveDateString(): String {
    if (this.isBlank()) return this

    return try {
        val date = LocalDate.parse(this, DATE_FORMAT_DASH)
        date.format(WISH_ARCHIVE_DATE_FMT)
    } catch (e: Exception) {
        this
    }
}