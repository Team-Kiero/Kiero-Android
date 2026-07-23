package com.kiero.core.analytic.type

import com.kiero.core.analytic.AnalyticsPropertyKey

sealed class KieroEvent(
    override val type: EventType,
    override val eventName: String,
    override val properties: Map<String, Any?> = emptyMap()
) : AnalyticsEvent {

    object AppOpened : KieroEvent(EventType.VIEW, "app_opened")

    object OnboardingCompleted : KieroEvent(EventType.VIEW, "onboarding_completed")

    class PushClicked(pushType: String, destinationScreen: DestinationScreen) : KieroEvent(
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

        class Created(
            scheduleId: String,
            isRecurring: Boolean,
            selectedDayCount: Int,
            durationMinutes: Int
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

        class AuthStarted(scheduleId: String) : Schedule(
            type = EventType.CLICK,
            eventName = "schedule_auth_started",
            properties = mapOf(AnalyticsPropertyKey.SCHEDULE_ID to scheduleId)
        )

        class AuthCompleted(scheduleId: String) : Schedule(
            type = EventType.SUBMIT,
            eventName = "schedule_auth_completed",
            properties = mapOf(AnalyticsPropertyKey.SCHEDULE_ID to scheduleId)
        )

        class Skipped(scheduleId: String) : Schedule(
            type = EventType.CLICK,
            eventName = "schedule_skipped",
            properties = mapOf(AnalyticsPropertyKey.SCHEDULE_ID to scheduleId)
        )

        class DailyJourneyCompleted(completedCount: Int, totalCount: Int) : Schedule(
            type = EventType.SUBMIT,
            eventName = "daily_journey_completed",
            properties = mapOf(
                AnalyticsPropertyKey.COMPLETED_SCHEDULE_COUNT to completedCount,
                AnalyticsPropertyKey.TOTAL_SCHEDULE_COUNT to totalCount
            )
        )
    }

    // 미션 (Mission)  이벤트
    sealed class Mission(
        type: EventType,
        eventName: String,
        properties: Map<String, Any?>
    ) : KieroEvent(type, eventName, properties) {

        class Created(
            creationMethod: CreationMethod,
            dueDateType: DueDateType,
            rewardGold: Int,
            missionCount: Int,
            missionId: String
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

        class Completed(rewardGold: Int, missionId: String) : Mission(
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

        class Created(rewardId: String, goldCost: Int) : Reward(
            type = EventType.SUBMIT,
            eventName = "reward_created",
            properties = mapOf(
                AnalyticsPropertyKey.REWARD_ID to rewardId,
                AnalyticsPropertyKey.GOLD_COST to goldCost
            )
        )

        class Purchased(rewardId: String, goldCost: Int) : Reward(
            type = EventType.SUBMIT,
            eventName = "wish_purchased",
            properties = mapOf(
                AnalyticsPropertyKey.REWARD_ID to rewardId,
                AnalyticsPropertyKey.GOLD_COST to goldCost
            )
        )
    }
}