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