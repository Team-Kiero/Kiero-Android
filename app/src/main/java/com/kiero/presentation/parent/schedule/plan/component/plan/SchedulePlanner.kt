package com.kiero.presentation.parent.schedule.plan.component.plan

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.model.ScheduleEvent
import com.kiero.presentation.parent.schedule.model.toDayIndex
import com.kiero.presentation.parent.schedule.model.toScheduleBlocks
import com.kiero.presentation.parent.schedule.plan.model.ScheduleData


@Composable
fun ScheduleTimeTable(
    events: List<ScheduleEvent>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        ScheduleWeekTopbar(
            dayList = ScheduleData.fakeDayList
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ScheduleTimeColumn()

            SchedulePlanner(events = events)
        }
    }
}

@Composable
fun SchedulePlanner(
    events: List<ScheduleEvent>,
    modifier: Modifier = Modifier,
    daysCount: Int = 7,
) {
    val density = LocalDensity.current
    val hourHeight = with(density) { 38.dp.roundToPx().toDp() }

    val allBlocks = events.flatMap { event ->
        val dayIndex = event.dayOfWeek?.toDayIndex() ?: 0
        event.toScheduleBlocks(dayIndex)
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(hourHeight * 15)
            .clip(RoundedCornerShape(10.dp))
            .border(0.3.dp, KieroTheme.colors.gray800, RoundedCornerShape(10.dp))
            .padding(4.dp)
    ) {
        val dayWidth = maxWidth / daysCount

        allBlocks.forEach { block ->
            val topOffset = hourHeight * (block.startHour - 8)
            val blockHeight = hourHeight * block.durationInSlots

            Box(
                modifier = Modifier
                    .offset(x = dayWidth * block.dayIndex, y = topOffset)
                    .width(dayWidth)
                    .height(blockHeight)
                    .padding(horizontal = 3.dp)
            ) {
                ScheduleEventBlock(block = block)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2D2F34)
@Composable
private fun ScheduleTimeTablePreview() {
    KieroTheme {
        ScheduleTimeTable(
            events = listOf(
                ScheduleEvent(
                    id = "1",
                    name = "학교",
                    isRecurring = true,
                    startTime = "08:00",
                    endTime = "12:00",
                    scheduleColor = "SCHEDULE4",
                    dayOfWeek = "MON",
                    date = null
                ),
                ScheduleEvent(
                    id = "2",
                    name = "수영",
                    isRecurring = true,
                    startTime = "08:00",
                    endTime = "13:00",
                    scheduleColor = "SCHEDULE2",
                    dayOfWeek = "TUE",
                    date = null
                ),
                ScheduleEvent(
                    id = "5",
                    name = "수영",
                    isRecurring = true,
                    startTime = "08:00",
                    endTime = "9:00",
                    scheduleColor = "SCHEDULE2",
                    dayOfWeek = "WED",
                    date = null
                ),
                ScheduleEvent(
                    id = "3",
                    name = "태권도",
                    isRecurring = true,
                    startTime = "14:00",
                    endTime = "16:00",
                    scheduleColor = "SCHEDULE2",
                    dayOfWeek = "TUE",
                    date = null
                ),
            )
        )
    }
}