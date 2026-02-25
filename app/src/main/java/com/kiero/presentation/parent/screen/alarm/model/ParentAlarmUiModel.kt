package com.kiero.presentation.parent.screen.alarm.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.kiero.core.common.extension.formattedAlarmDate
import com.kiero.core.common.extension.formattedAlarmTime
import com.kiero.core.common.extension.withJosa
import com.kiero.data.parent.alarm.model.AlarmItemModel

private object AlarmUiConstant {
    val HIGHLIGHT_MAIN = Color(0xFF00FFE1)
    val HIGHLIGHT_WHITE = Color(0xFFFFFFFF)

    const val TEXT_COMPLETE_KEY = "complete"
    const val TEXT_DAILY_SCHEDULE = "하루 일정"
}

@Immutable
data class ParentAlarmUiModel(
    val id: String,
    val date: String,
    val time: String,
    val message: String,
    val highlightText: String,
    val highlightColor: Color,
    val coinUsed: Int?,
    val imageUrl: String?,
    val isExpanded: Boolean = false
)

fun AlarmItemModel.toUiModel(childName: String): ParentAlarmUiModel {
    val contentKey = when (this) {
        is AlarmItemModel.Schedule -> locationName
        is AlarmItemModel.Mission -> missionName
        is AlarmItemModel.Coupon -> couponName
        is AlarmItemModel.Complete -> AlarmUiConstant.TEXT_COMPLETE_KEY
    }

    val uniqueId = "${occurredAt}_${this::class.simpleName}_${contentKey.hashCode()}"
    val subject = childName.withJosa("이가")

    val dateStr = occurredAt.formattedAlarmDate
    val timeStr = occurredAt.formattedAlarmTime

    return when (this) {
        is AlarmItemModel.Schedule -> ParentAlarmUiModel(
            id = uniqueId,
            date = dateStr,
            time = timeStr,
            message = "$subject ${locationName}에 도착했어요.",
            highlightText = locationName,
            highlightColor = AlarmUiConstant.HIGHLIGHT_MAIN,
            coinUsed = null,
            imageUrl = imageUrl
        )
        is AlarmItemModel.Mission -> ParentAlarmUiModel(
            id = uniqueId,
            date = dateStr,
            time = timeStr,
            message = "$subject $missionName 미션을 완료했어요.",
            highlightText = missionName,
            highlightColor = AlarmUiConstant.HIGHLIGHT_MAIN,
            coinUsed = rewardAmount,
            imageUrl = null
        )
        is AlarmItemModel.Coupon -> ParentAlarmUiModel(
            id = uniqueId,
            date = dateStr,
            time = timeStr,
            message = "$subject $couponName 쿠폰을 사용했어요.",
            highlightText = couponName,
            highlightColor = AlarmUiConstant.HIGHLIGHT_MAIN,
            coinUsed = usedAmount,
            imageUrl = null
        )
        is AlarmItemModel.Complete -> ParentAlarmUiModel(
            id = uniqueId,
            date = dateStr,
            time = timeStr,
            message = "$subject ${AlarmUiConstant.TEXT_DAILY_SCHEDULE}을 모두 완료했어요.",
            highlightText = AlarmUiConstant.TEXT_DAILY_SCHEDULE,
            highlightColor = AlarmUiConstant.HIGHLIGHT_WHITE,
            coinUsed = if (rewardAmount > 0) rewardAmount else null,
            imageUrl = null
        )
    }
}