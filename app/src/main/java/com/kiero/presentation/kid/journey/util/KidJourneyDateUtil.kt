package com.kiero.presentation.kid.journey.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object KidJourneyDateUtil {

    private val dateFormatter = DateTimeFormatter.ofPattern("M월 d일 E요일", Locale.KOREAN)

    fun formatDate(date: LocalDate): String {
        return date.format(dateFormatter)
    }
}