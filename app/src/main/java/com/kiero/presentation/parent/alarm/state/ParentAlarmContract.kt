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
    val hasMore: Boolean = true
) {
    companion object {
        val FAKE = AlarmFeedState(
            isLoading = false,
            alarms = persistentListOf(
                ParentAlarmUiModel(
                    id = "1",
                    time = "오늘 12 : 00",
                    message = "근영이가 피아노 학원에 도착했어요.",
                    highlightText = "피아노 학원",
                    highlightColor = Color(0xFF00E5FF),
                    coinUsed = 150,
                    imageUrl = "https://example.com/image.jpg",
                    isExpanded = false
                ),
                ParentAlarmUiModel(
                    id = "2",
                    time = "오늘 14 : 30",
                    message = "미션 '방 청소하기'를 완료하고 100개 금화를 받았어요.",
                    highlightText = "100개",
                    highlightColor = Color(0xFFFFB84D),
                    coinUsed = 100,
                    imageUrl = null,
                    isExpanded = false
                )
            )
        )
    }
}