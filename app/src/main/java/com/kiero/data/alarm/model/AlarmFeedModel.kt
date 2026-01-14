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
        // ✅ 1. 서버 ID가 없으면 데이터 조합(날짜+타입+해시)으로 고유 ID 생성
        // 이 작업이 없으면 LazyColumn이 리스트를 그리지 못합니다.
        val fallbackId = (occurredAt + eventType + metadata.toString()).hashCode().toLong()
        val finalId = id ?: fallbackId

        when (eventType) {
            "SCHEDULE" -> AlarmItemModel.Schedule(
                id = finalId, // ✅ 생성한 ID 주입
                occurredAt = occurredAt,
                locationName = metadata["content"]?.jsonPrimitive?.content ?: "",
                imageUrl = metadata["imageUrl"]?.jsonPrimitive?.content
            )

            "MISSION" -> AlarmItemModel.Mission(
                id = finalId,
                occurredAt = occurredAt,
                missionName = metadata["content"]?.jsonPrimitive?.content ?: "",
                rewardAmount = metadata["amount"]?.jsonPrimitive?.intOrNull ?: 0
            )

            "COUPON" -> AlarmItemModel.Coupon(
                id = finalId,
                occurredAt = occurredAt,
                couponName = metadata["content"]?.jsonPrimitive?.content ?: "",
                usedAmount = metadata["amount"]?.jsonPrimitive?.intOrNull ?: 0
            )

            "COMPLETE" -> AlarmItemModel.Complete(
                id = finalId,
                occurredAt = occurredAt,
                rewardAmount = metadata["amount"]?.jsonPrimitive?.intOrNull ?: 0
            )

            else -> {
                Timber.w("Unknown eventType: $eventType")
                null
            }
        }
    } catch (e: Exception) {
        Timber.e(e, "파싱 실패 - eventType: $eventType, 데이터: $metadata")
        null
    }
}