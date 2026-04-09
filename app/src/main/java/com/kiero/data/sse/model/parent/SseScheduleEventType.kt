package com.kiero.data.sse.model.parent

enum class SseScheduleEventType(
    val value: String
) {
    SCHEDULE_STATUS_UPDATED("SCHEDULE_STATUS_UPDATED"),
    FIRE_LIT("FIRE_LIT"),
    TODAY_MISSION_COMPLETED("TODAY_MISSION_COMPLETED"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun from(value: String?): SseScheduleEventType {
            return entries.find { it.value == value } ?: UNKNOWN
        }
    }
}
