package com.kiero.presentation.parent.screen.schedule.plan.component.plan

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.emptyview.KieroEmptyView
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.schedule.model.ScheduleEvent
import com.kiero.presentation.parent.screen.schedule.model.toScheduleBlocks
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleState

@Composable
fun ScheduleTimeTable(
    state: ParentScheduleState,
    events: List<ScheduleEvent>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            ScheduleTimeColumn()

            SchedulePlanner(events = events, state = state)
        }
    }
}

@Composable
fun SchedulePlanner(
    events: List<ScheduleEvent>,
    state: ParentScheduleState,
    modifier: Modifier = Modifier,
    daysCount: Int = 7,
) {
    val density = LocalDensity.current
    val hourHeight = with(density) { 38.dp.roundToPx().toDp() }
    val slotHeight = hourHeight / 4

    val innerPadding = 4.dp
    val bottomBuffer = 4.dp
    val totalHeight = (hourHeight * 14) + (innerPadding * 2) + bottomBuffer

    val allBlocks = events.flatMap { event ->
        val indices = state.run { event.getIndices() }

        indices.flatMap { index ->
            event.toScheduleBlocks(index)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHeight)
            .clip(RoundedCornerShape(10.dp))
            .border(0.3.dp, KieroTheme.colors.gray800, RoundedCornerShape(10.dp))
            .padding(innerPadding)
    ) {
        val dayWidth = maxWidth / daysCount
        if (events.isEmpty()) {
            KieroEmptyView()
        } else {
            allBlocks.forEach { block ->
                val hourOffset = hourHeight * (block.startHour - 8)
                val minuteOffset = slotHeight * (block.startMinute / 15)
                val topOffset = hourOffset + minuteOffset

                val blockHeight = slotHeight * block.durationInSlots
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
        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2D2F34)
@Composable
private fun ScheduleTimeTablePreview() {
    val mockEvents = ScheduleEvent.empty()

    KieroTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ScheduleTimeTable(
                state = ParentScheduleState(),
                events = mockEvents
            )
        }
    }
}