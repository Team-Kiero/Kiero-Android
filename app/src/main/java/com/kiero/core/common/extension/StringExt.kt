package com.kiero.core.common.extension

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
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

/**
 * ISO_LOCAL_DATE_TIME 형식을 날짜 헤더 형식으로 변환
 * 예: "2026-01-10T14:30:00" → "2026.01.10.(금)"
 */
val String.formattedAlarmDate: String
    get() {
        if (this.isBlank()) return ""

        return try {
            val dateTime = LocalDateTime.parse(this)
            val dayOfWeek = when (dateTime.dayOfWeek) {
                DayOfWeek.MONDAY -> "월"
                DayOfWeek.TUESDAY -> "화"
                DayOfWeek.WEDNESDAY -> "수"
                DayOfWeek.THURSDAY -> "목"
                DayOfWeek.FRIDAY -> "금"
                DayOfWeek.SATURDAY -> "토"
                DayOfWeek.SUNDAY -> "일"
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            "${dateTime.format(formatter)}.($dayOfWeek)"
        } catch (e: Exception) {
            this
        }
    }

/**
 * ISO_LOCAL_DATE_TIME 형식을 시간 표시 형식으로 변환
 * 오늘: "오늘 14 : 30"
 * 어제: "어제 14 : 30"
 * 그 외: "14 : 30"
 */
val String.formattedAlarmTime: String
    get() {
        if (this.isBlank()) return ""

        return try {
            val dateTime = LocalDateTime.parse(this)
            val date = dateTime.toLocalDate()
            val today = LocalDate.now()
            val yesterday = today.minusDays(1)

            val timeFormatter = DateTimeFormatter.ofPattern("HH : mm")
            val timeString = dateTime.format(timeFormatter)

            when {
                date.isEqual(today) -> "오늘 $timeString"
                date.isEqual(yesterday) -> "어제 $timeString"
                else -> timeString
            }
        } catch (e: Exception) {
            this
        }
    }

/**
 * 특정 키워드에 하이라이트 색상을 입힌 AnnotatedString을 반환합니다.
 * 여러 화면 뷰에서 하이라이트된 글자가 있어서 공통으로 뺐습니다.
 * EX : 오늘의 여정 - "name": "피아노 학원 가기",
 */
fun String.toHighlightedText(
    highlightColor: Color,
    vararg highlightTexts: String? // null 대응을 위해 String? 사용
): AnnotatedString = buildAnnotatedString {
    append(this@toHighlightedText)

    // 가변 인자(vararg)를 순회하며 스타일 적용
    highlightTexts.forEach { target ->
        if (target.isNullOrBlank()) return@forEach

        var startIndex = indexOf(target)
        while (startIndex >= 0) {
            addStyle(
                style = SpanStyle(color = highlightColor),
                start = startIndex,
                end = startIndex + target.length
            )
            // 다음 동일 키워드 검색 (중복 단어 대응)
            startIndex = indexOf(target, startIndex + target.length)
        }
    }
}