package com.kiero.presentation.parent.schedule.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt


private const val HOUR_START = 8
private const val SLOT_PER_HOUR = 4
private const val MINUTE_UNIT = 15

fun ScheduleEvent.toScheduleBlocks(dayIndex: Int): List<ScheduleBlock> {
    val (startHour, startMinute) = parseTime(startTime)
    val (endHour, endMinute) = parseTime(endTime)

    val startSlot = ((startHour - HOUR_START) * SLOT_PER_HOUR) + (startMinute / MINUTE_UNIT)
    val endSlot = ((endHour - HOUR_START) * SLOT_PER_HOUR) + (endMinute / MINUTE_UNIT)
    val totalSlots = endSlot - startSlot

    val color = try {
        if (scheduleColor.startsWith("#")) {
            Color(scheduleColor.toColorInt())
        } else {
            when (scheduleColor) {
                "SCHEDULE1" -> ScheduleColorType.SCHEDULE1.color
                "SCHEDULE2" -> ScheduleColorType.SCHEDULE2.color
                "SCHEDULE3" -> ScheduleColorType.SCHEDULE3.color
                "SCHEDULE4" -> ScheduleColorType.SCHEDULE4.color
                "SCHEDULE5" -> ScheduleColorType.SCHEDULE5.color
                else -> ScheduleColorType.SCHEDULE1.color
            }
        }
    } catch (e: Exception) {
        ScheduleColorType.SCHEDULE1.color
    }

    if (totalSlots < 1) return emptyList()

    val blocks = mutableListOf<ScheduleBlock>()
    var currentSlot = startSlot
    var blockIndex = 0

    while (currentSlot < endSlot) {
        val remainingSlots = endSlot - currentSlot
        val blockSlots = minOf(4, remainingSlots)

        val position = when {
            blockIndex == 0 && currentSlot + blockSlots >= endSlot -> BlockPosition.SINGLE
            blockIndex == 0 -> BlockPosition.TOP
            currentSlot + blockSlots >= endSlot -> BlockPosition.BOTTOM
            else -> BlockPosition.MIDDLE
        }

        val currentHour = HOUR_START + (currentSlot / SLOT_PER_HOUR)
        val currentMinute = (currentSlot % SLOT_PER_HOUR) * MINUTE_UNIT

        blocks.add(
            ScheduleBlock(
                id = if (blockIndex == 0) id else "$id-$blockIndex",
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

fun parseTime(timeString: String): Pair<Int, Int> {
    val parts = timeString.split(":")
    return Pair(parts[0].toInt(), parts[1].toInt())
}