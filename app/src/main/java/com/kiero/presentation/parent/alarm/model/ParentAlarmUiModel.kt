package com.kiero.presentation.parent.alarm.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


@Immutable
data class ParentAlarmUiModel(
    val id: String,
    val date: String, // 날짜 헤더 표시용. 예: "2025.12.26.(금)"
    val time: String,
    val message: String,
    val highlightText: String,
    val highlightColor: Color,
    val coinUsed: Int?,
    val imageUrl: Any?,  // String? → Any? (Int 리소스 ID도 받을 수 있게)
    val isExpanded: Boolean = false
)