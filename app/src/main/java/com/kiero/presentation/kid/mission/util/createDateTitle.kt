package com.kiero.presentation.kid.mission.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun createDateTitle(dueAt: String, dayOfWeek: String): String {
    return try {
        val date = LocalDate.parse(dueAt, DateTimeFormatter.ISO_DATE)
        val today = LocalDate.now()

        if (date.isEqual(today)) {
            "오늘까지"
        } else {
            "${date.monthValue}월 ${date.dayOfMonth}일 ${dayOfWeek}까지"
        }
    } catch (e: Exception) {
        "$dueAt ${dayOfWeek}까지"
    }
}