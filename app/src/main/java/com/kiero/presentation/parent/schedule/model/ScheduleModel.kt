package com.kiero.presentation.parent.schedule.model

import androidx.compose.ui.graphics.Color

enum class ScheduleColorType(val color: Color) {
    SCHEDULE1(Color(0xFFCFFFFA)),
    SCHEDULE2(Color(0xFFFFFEE9)),
    SCHEDULE3(Color(0xFFBFFFE3)),
    SCHEDULE4(Color(0xFF34D9D3)),
    SCHEDULE5(Color(0xFF7BBDFF)),
}

enum class TabItem(val title: String) {
    SCHEDULE("일정"),
    MISSION("미션"),
}


enum class BlockPosition {
    SINGLE,
    TOP,
    MIDDLE,
    BOTTOM
}
