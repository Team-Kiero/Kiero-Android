package com.kiero.presentation.kid.journey.map.model

enum class KidMapScheduleStatus(val serverKey: String) {
    PENDING("PENDING"),
    COMPLETE("COMPLETE"),
    FAILED("FAILED"),
    SKIPPED("SKIPPED"),
    VERIFIED("VERIFIED");

    companion object {
        fun from(key: String): KidMapScheduleStatus =
            entries.find { it.serverKey == key } ?: PENDING
    }
}