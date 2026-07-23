package com.kiero.core.analytic

object AnalyticsPropertyKey {
    // 일정
    const val SCHEDULE_ID = "schedule_id"
    const val IS_RECURRING = "is_recurring"
    const val SELECTED_DAY_COUNT = "selected_day_count"
    const val DURATION_MINUTES = "duration_minutes"
    const val COMPLETED_SCHEDULE_COUNT = "completed_schedule_count"
    const val TOTAL_SCHEDULE_COUNT = "total_schedule_count"

    // 미션
    const val MISSION_ID = "mission_id"
    const val MISSION_COUNT = "mission_count"
    const val CREATION_METHOD = "creation_method"
    const val DUE_DATE_TYPE = "due_date_type"

    // 보상
    const val REWARD_ID = "reward_id"
    const val GOLD_COST = "gold_cost"
    const val REWARD_GOLD = "reward_gold"

    // 알람
    const val PUSH_TYPE = "push_type"
    const val DESTINATION_SCREEN = "destination_screen"
}