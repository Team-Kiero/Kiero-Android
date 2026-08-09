package com.kiero.core.analytic.event

import com.kiero.core.analytic.event.AnalyticsPropertyKey
import com.kiero.core.analytic.event.CreationMethod
import com.kiero.core.analytic.event.DestinationScreen
import com.kiero.core.analytic.event.DueDateType
import com.kiero.core.analytic.type.AnalyticsEvent
import com.kiero.core.analytic.type.EventType

sealed class KieroEvent(
    override val type: EventType,
    override val eventName: String,
    override val properties: Map<String, Any?>
) : AnalyticsEvent {

    object AppOpened : KieroEvent(EventType.VIEW, "app_opened", emptyMap())

    object OnboardingCompleted : KieroEvent(EventType.VIEW, "onboarding_completed", emptyMap())


    data class PushClicked(val pushType: String, val destinationScreen: DestinationScreen) : KieroEvent(
        type = EventType.CLICK,
        eventName = "push_clicked",
        properties = mapOf(
            AnalyticsPropertyKey.PUSH_TYPE to pushType,
            AnalyticsPropertyKey.DESTINATION_SCREEN to destinationScreen.value
        )
    )

    // 일정 (Schedule) 이벤트
    sealed class Schedule(
        type: EventType,
        eventName: String,
        properties: Map<String, Any?>
    ) : KieroEvent(type, eventName, properties) {

        data class Created(
            val scheduleId: String,
            val isRecurring: Boolean,
            val selectedDayCount: Int,
            val durationMinutes: Int
        ) : Schedule(
            type = EventType.SUBMIT,
            eventName = "schedule_created",
            properties = mapOf(
                AnalyticsPropertyKey.SCHEDULE_ID to scheduleId,
                AnalyticsPropertyKey.IS_RECURRING to isRecurring,
                AnalyticsPropertyKey.SELECTED_DAY_COUNT to selectedDayCount,
                AnalyticsPropertyKey.DURATION_MINUTES to durationMinutes
            )
        )

        data class AuthStarted(val scheduleId: String) : Schedule(
            type = EventType.CLICK,
            eventName = "schedule_auth_started",
            properties = mapOf(AnalyticsPropertyKey.SCHEDULE_ID to scheduleId)
        )

        data class AuthCompleted(val scheduleId: String) : Schedule(
            type = EventType.SUBMIT,
            eventName = "schedule_auth_completed",
            properties = mapOf(AnalyticsPropertyKey.SCHEDULE_ID to scheduleId)
        )

        data class Skipped(val scheduleId: String) : Schedule(
            type = EventType.CLICK,
            eventName = "schedule_skipped",
            properties = mapOf(AnalyticsPropertyKey.SCHEDULE_ID to scheduleId)
        )

        data class DailyJourneyCompleted(val completedCount: Int, val totalCount: Int) : Schedule(
            type = EventType.SUBMIT,
            eventName = "daily_journey_completed",
            properties = mapOf(
                AnalyticsPropertyKey.COMPLETED_SCHEDULE_COUNT to completedCount,
                AnalyticsPropertyKey.TOTAL_SCHEDULE_COUNT to totalCount
            )
        )
    }

    // 미션 (Mission) 이벤트
    sealed class Mission(
        type: EventType,
        eventName: String,
        properties: Map<String, Any?>
    ) : KieroEvent(type, eventName, properties) {

        data class Created(
            val creationMethod: CreationMethod,
            val dueDateType: DueDateType,
            val rewardGold: Int,
            val missionCount: Int,
            val missionId: String
        ) : Mission(
            type = EventType.SUBMIT,
            eventName = "mission_created",
            properties = mapOf(
                AnalyticsPropertyKey.CREATION_METHOD to creationMethod.value,
                AnalyticsPropertyKey.DUE_DATE_TYPE to dueDateType.value,
                AnalyticsPropertyKey.REWARD_GOLD to rewardGold,
                AnalyticsPropertyKey.MISSION_COUNT to missionCount,
                AnalyticsPropertyKey.MISSION_ID to missionId
            )
        )

        data class Completed(val rewardGold: Int, val missionId: String) : Mission(
            type = EventType.SUBMIT,
            eventName = "mission_completed",
            properties = mapOf(
                AnalyticsPropertyKey.REWARD_GOLD to rewardGold,
                AnalyticsPropertyKey.MISSION_ID to missionId
            )
        )
    }

    // 보상 이벤트
    sealed class Reward(
        type: EventType,
        eventName: String,
        properties: Map<String, Any?>
    ) : KieroEvent(type, eventName, properties) {

        data class Created(val rewardId: String, val goldCost: Int) : Reward(
            type = EventType.SUBMIT,
            eventName = "reward_created",
            properties = mapOf(
                AnalyticsPropertyKey.REWARD_ID to rewardId,
                AnalyticsPropertyKey.GOLD_COST to goldCost
            )
        )

        data class Purchased(val rewardId: String, val goldCost: Int) : Reward(
            type = EventType.SUBMIT,
            eventName = "wish_purchased",
            properties = mapOf(
                AnalyticsPropertyKey.REWARD_ID to rewardId,
                AnalyticsPropertyKey.GOLD_COST to goldCost
            )
        )
    }
}