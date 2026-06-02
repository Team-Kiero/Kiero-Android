package com.kiero.core.model.fcm

data class PushData(
    val type: PushType,
    val targetId: String? = null
)

enum class PushType {
    PARENT_DAILY_START,
    SCHEDULE_SKIPPED,
    PARENT_SCHEDULE_REMINDER,

    COUPON_PURCHASED,
    FIRE_LIT,
    MISSION_COMPLETE,
    SCHEDULE_VERIFIED,

    CHILD_DAILY_START,
    CHILD_NEXT_JOURNEY,
    SCHEDULE_CREATED,
    SCHEDULE_DELETED,
    SCHEDULE_MODIFIED,

    CHILD_MISSION_INCOMPLETE,

    UNKNOWN;

    companion object {
        fun fromString(value: String?): PushType {
            return entries.find { it.name == value } ?: UNKNOWN
        }
    }
}