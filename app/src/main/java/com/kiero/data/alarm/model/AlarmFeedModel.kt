package com.kiero.data.alarm.model

import com.kiero.data.alarm.dto.response.AlarmFeedResponseDto
import com.kiero.data.alarm.dto.response.FeedItemDto
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.intOrNull  // ← 추가!
import timber.log.Timber  // ← 추가!

data class AlarmFeedModel(
    val childName: String = "",
    val items: List<AlarmItemModel> = emptyList(),
    val nextCursor: String? = null
)

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

fun AlarmFeedResponseDto.toModel() = AlarmFeedModel(
    childName = this.childName,
    items = this.feedItems.mapNotNull { it.toModel() },
    nextCursor = this.nextCursor
)

fun FeedItemDto.toModel(): AlarmItemModel? {
    return try {
        val fallbackId = (occurredAt + eventType + metadata.toString()).hashCode().toLong()
        val finalId = id ?: fallbackId

        when (eventType) {
            "SCHEDULE" -> AlarmItemModel.Schedule(
                id = finalId,
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