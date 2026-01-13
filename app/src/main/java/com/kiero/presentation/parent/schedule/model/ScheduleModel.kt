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

data class ScheduleEvent(
    val id: String,
    val name: String,
    val isRecurring: Boolean,
    val startTime: String,
    val endTime: String,
    val scheduleColor: String,
    val dayOfWeek: String?,
    val date: String?,
)

data class ScheduleBlock(
    val id: String,
    val title: String,
    val color: Color,
    val startHour: Int,
    val startMinute: Int,
    val durationInSlots: Int,
    val dayIndex: Int,
    val blockPosition: BlockPosition
)

enum class BlockPosition {
    SINGLE,
    TOP,
    MIDDLE,
    BOTTOM
}

fun parseTime(timeString: String): Pair<Int, Int> {
    val parts = timeString.split(":")
    return Pair(parts[0].toInt(), parts[1].toInt())
}

fun ScheduleEvent.toScheduleBlocks(dayIndex: Int): List<ScheduleBlock> {
    val (startHour, startMinute) = parseTime(startTime)
    val (endHour, endMinute) = parseTime(endTime)

    val startSlot = ((startHour - 8) * 2) + (startMinute / 30)
    val endSlot = ((endHour - 8) * 2) + (endMinute / 30)
    val totalSlots = endSlot - startSlot

    val color = when (scheduleColor) {
        "SCHEDULE1" -> ScheduleColorType.SCHEDULE1.color
        "SCHEDULE2" -> ScheduleColorType.SCHEDULE2.color
        "SCHEDULE3" -> ScheduleColorType.SCHEDULE3.color
        "SCHEDULE4" -> ScheduleColorType.SCHEDULE4.color
        "SCHEDULE5" -> ScheduleColorType.SCHEDULE5.color
        else -> ScheduleColorType.SCHEDULE1.color
    }

    if (totalSlots <= 2) {
        return listOf(
            ScheduleBlock(
                id = id,
                title = name,
                color = color,
                startHour = startHour,
                startMinute = startMinute,
                durationInSlots = totalSlots,
                dayIndex = dayIndex,
                blockPosition = BlockPosition.SINGLE
            )
        )
    }

    val blocks = mutableListOf<ScheduleBlock>()
    var currentSlot = startSlot
    var blockIndex = 0

    while (currentSlot < endSlot) {
        val remainingSlots = endSlot - currentSlot
        val blockSlots = minOf(2, remainingSlots)

        val position = when {
            blockIndex == 0 -> BlockPosition.TOP
            currentSlot + blockSlots >= endSlot -> BlockPosition.BOTTOM
            else -> BlockPosition.MIDDLE
        }

        val currentHour = 8 + (currentSlot / 2)
        val currentMinute = (currentSlot % 2) * 30

        blocks.add(
            ScheduleBlock(
                id = "$id-$blockIndex",
                title = if (blockIndex == 0) name else "",
                color = color,
                startHour = currentHour,
                startMinute = currentMinute,
                durationInSlots = blockSlots,
                dayIndex = dayIndex,
                blockPosition = position
            )
        )

        currentSlot += blockSlots
        blockIndex++
    }

    return blocks
}

fun String.toDayIndex(): Int {
    return when (this) {
        "MON" -> 0
        "TUE" -> 1
        "WED" -> 2
        "THU" -> 3
        "FRI" -> 4
        "SAT" -> 5
        "SUN" -> 6
        else -> 0
    }
}