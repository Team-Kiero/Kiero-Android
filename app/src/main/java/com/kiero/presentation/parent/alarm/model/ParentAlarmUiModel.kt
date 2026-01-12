package com.kiero.presentation.parent.alarm.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


@Immutable
data class ParentAlarmUiModel(
    val id: String,
    val time: String,
    val message: String,
    val highlightText: String,
    val highlightColor: Color,
    val coinUsed: Int?,
    val imageUrl: String?,
    val isExpanded: Boolean = false
)