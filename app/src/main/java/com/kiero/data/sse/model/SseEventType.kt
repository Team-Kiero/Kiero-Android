package com.kiero.data.sse.model

enum class SseEventType(val value: String) {
    CONNECTED("connected"),
    HEARTBEAT("heartbeat"),
    INVITE("invite"),
    FEED("feed"),
    MISSION("mission"),
    SCHEDULE("schedule"),
    COUPON("coupon"),
    DATE("date"),
    UNKNOWN("unknown");

    companion object {
        fun from(type: String?): SseEventType {
            return entries.find { it.value == type } ?: UNKNOWN
        }
    }
}