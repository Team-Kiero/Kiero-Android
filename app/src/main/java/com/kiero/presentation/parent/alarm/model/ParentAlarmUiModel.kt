package com.kiero.presentation.parent.alarm.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.kiero.core.common.extension.formattedAlarmDate
import com.kiero.core.common.extension.formattedAlarmTime
import com.kiero.data.alarm.model.AlarmItemModel

@Immutable
data class ParentAlarmUiModel(
    val id: String,
    val date: String,
    val time: String,
    val message: String,
    val highlightText: String,
    val highlightColor: Color,
    val coinUsed: Int?,
    val imageUrl: Any?,
    val isExpanded: Boolean = false
)

fun AlarmItemModel.toUiModel(childName: String): ParentAlarmUiModel {
    val highlightColor = Color(0xFF00FFE1)

    val contentKey = when (this) {
        is AlarmItemModel.Schedule -> locationName
        is AlarmItemModel.Mission -> missionName
        is AlarmItemModel.Coupon -> couponName
        is AlarmItemModel.Complete -> "complete"
    }
    val uniqueId = "${occurredAt}_${this::class.simpleName}_${contentKey.hashCode()}"

    // TODO: 아이 이름 뒤 '이/가' 조사 자동 처리 유틸리티 적용 필요
    return when (this) {
        is AlarmItemModel.Schedule -> ParentAlarmUiModel(
            id = uniqueId,
            date = occurredAt.formattedAlarmDate,
            time = occurredAt.formattedAlarmTime,
            message = "${childName}가 ${locationName}에 도착했어요.",
            highlightText = locationName, highlightColor = highlightColor,
            coinUsed = null, imageUrl = imageUrl, isExpanded = false
        )
        is AlarmItemModel.Mission -> ParentAlarmUiModel(
            id = uniqueId,
            date = occurredAt.formattedAlarmDate,
            time = occurredAt.formattedAlarmTime,
            message = "${childName}가 '$missionName' 미션을 완료했어요.",
            highlightText = missionName, highlightColor = highlightColor,
            coinUsed = rewardAmount, imageUrl = null, isExpanded = false
        )
        is AlarmItemModel.Coupon -> ParentAlarmUiModel(
            id = uniqueId,
            date = occurredAt.formattedAlarmDate,
            time = occurredAt.formattedAlarmTime,
            message = "${childName}가 '$couponName' 쿠폰을 사용했어요.",
            highlightText = couponName, highlightColor = highlightColor,
            coinUsed = usedAmount, imageUrl = null, isExpanded = false
        )
        is AlarmItemModel.Complete -> ParentAlarmUiModel(
            id = uniqueId,
            date = occurredAt.formattedAlarmDate,
            time = occurredAt.formattedAlarmTime,
            message = "${childName}가 하루 일정을 모두 완료했어요.",
            highlightText = "하루 일정", highlightColor = highlightColor,
            coinUsed = if (rewardAmount > 0) rewardAmount else null, imageUrl = null, isExpanded = false
        )
    }
}