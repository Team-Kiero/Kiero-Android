package com.kiero.presentation.parent.alarm.state


import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.kiero.presentation.parent.alarm.model.ParentAlarmUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class AlarmFeedState(
    val isLoading: Boolean = false,
    val alarms: ImmutableList<ParentAlarmUiModel> = persistentListOf(),
    val errorMessage: String? = null,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    /** 다음 커서 (페이징용) */
    val nextCursor: String? = null
) {
    companion object {
        val FAKE = AlarmFeedState(
            isLoading = false, alarms = persistentListOf(
                // 2025.12.26.(금) 알람들
                ParentAlarmUiModel(
                    id = "1",
                    date = "2025.12.26.(금)",
                    time = "12 : 00",
                    message = "근영이가 피아노 학원에 도착했어요.",
                    highlightText = "피아노 학원",
                    highlightColor = Color(0xFF00FFE1),
                    coinUsed = null,
                    imageUrl = "https://example.com/piano.jpg",
                    isExpanded = false
                ), ParentAlarmUiModel(
                    id = "2",
                    date = "2025.12.26.(금)",
                    time = "14 : 30",
                    message = "미션 '방 청소하기'를 완료하고 100개 금화를 받았어요.",
                    highlightText = "방 청소하기",
                    highlightColor = Color(0xFF00FFE1),
                    coinUsed = 100,
                    imageUrl = null,
                    isExpanded = false
                ), ParentAlarmUiModel(
                    id = "3",
                    date = "2025.12.26.(금)",
                    time = "16 : 00",
                    message = "쿠폰 '게임 30분 추가'를 사용하여 50개 금화를 받았어요.",
                    highlightText = "게임 30분 추가",
                    highlightColor = Color(0xFF00FFE1),
                    coinUsed = 50,
                    imageUrl = null,
                    isExpanded = false
                ),
                // 2025.12.25.(목) 알람들
                ParentAlarmUiModel(
                    id = "4",
                    date = "2025.12.25.(목)",
                    time = "09 : 30",
                    message = "근영이가 목표를 완료하고 200개 금화를 받았어요.",
                    highlightText = "200개",
                    highlightColor = Color(0xFF00FFE1),
                    coinUsed = 200,
                    imageUrl = null,
                    isExpanded = false
                ), ParentAlarmUiModel(
                    id = "5",
                    date = "2025.12.25.(목)",
                    time = "15 : 00",
                    message = "근영이가 수학 학원에 도착했어요.",
                    highlightText = "수학 학원",
                    highlightColor = Color(0xFF00FFE1),
                    coinUsed = null,
                    imageUrl = "https://example.com/math.jpg",
                    isExpanded = false
                )
            ), hasMore = true, nextCursor = "cursor_123"
        )
    }
}