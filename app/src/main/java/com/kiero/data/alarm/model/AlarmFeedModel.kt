package com.kiero.data.alarm.model

import com.kiero.data.alarm.dto.response.AlarmFeedResponseDto
import com.kiero.data.alarm.dto.response.FeedItemDto
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.intOrNull  // ← 추가!
import timber.log.Timber  // ← 추가!

/**
 * 알람 피드 도메인 모델
 */
data class AlarmFeedModel(
    val childName: String = "",
    val items: List<AlarmItemModel> = emptyList(),
    val nextCursor: String? = null
)

/**
 * EventType별 타입 안전 분리
 */
sealed class AlarmItemModel {
    abstract val id: Long
    abstract val occurredAt: String

    data class Schedule(
        override val id: Long,
        override val occurredAt: String = "",
        val locationName: String = "",
        val imageUrl: String? = null
    ) : AlarmItemModel()

    data class Mission(
        override val id: Long,
        override val occurredAt: String = "",
        val missionName: String = "",
        val rewardAmount: Int = 0
    ) : AlarmItemModel()

    data class Coupon(
        override val id: Long,
        override val occurredAt: String = "",
        val couponName: String = "",
        val usedAmount: Int = 0
    ) : AlarmItemModel()

    data class Complete(
        override val id: Long,
        override val occurredAt: String = "",
        val rewardAmount: Int = 0
    ) : AlarmItemModel()
}

/**
 * DTO → Domain Model 변환
 */
fun AlarmFeedResponseDto.toModel() = AlarmFeedModel(
    childName = this.childName,
    items = this.feedItems.mapNotNull { it.toModel() },
    nextCursor = this.nextCursor
)

/**
 * EventType별 파싱 로직
 * 파싱 실패 시 Timber로 로깅
 */
fun FeedItemDto.toModel(): AlarmItemModel? {
    return try {
        when (eventType) {
            "SCHEDULE" -> AlarmItemModel.Schedule(
                id = id,
                occurredAt = occurredAt,
                locationName = metadata["content"]?.jsonPrimitive?.content ?: "",
                imageUrl = metadata["imageUrl"]?.jsonPrimitive?.content
            )

            "MISSION" -> AlarmItemModel.Mission(
                id = id,
                occurredAt = occurredAt,
                missionName = metadata["content"]?.jsonPrimitive?.content ?: "",
                rewardAmount = metadata["amount"]?.jsonPrimitive?.intOrNull ?: 0  // ← intOrNull 사용
            )

            "COUPON" -> AlarmItemModel.Coupon(
                id = id,
                occurredAt = occurredAt,
                couponName = metadata["content"]?.jsonPrimitive?.content ?: "",
                usedAmount = metadata["amount"]?.jsonPrimitive?.intOrNull ?: 0  // ← intOrNull 사용
            )

            "COMPLETE" -> AlarmItemModel.Complete(
                id = id,
                occurredAt = occurredAt,
                rewardAmount = metadata["amount"]?.jsonPrimitive?.intOrNull ?: 0  // ← intOrNull 사용
            )

            else -> {
                // ✅ 알 수 없는 타입 로깅
                Timber.w("Unknown eventType: $eventType, occurredAt: $occurredAt")
                null
            }
        }
    } catch (e: Exception) {
        // 파싱 실패 로깅
        Timber.e(e, "Failed to parse FeedItemDto - eventType: $eventType, occurredAt: $occurredAt")
        null
    }
}