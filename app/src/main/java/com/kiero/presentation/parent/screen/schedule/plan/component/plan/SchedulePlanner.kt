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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.emptyview.KieroContentEmptyScreen
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.schedule.model.BlockPosition
import com.kiero.presentation.parent.screen.schedule.model.ScheduleEvent
import com.kiero.presentation.parent.screen.schedule.model.toScheduleBlocks
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleState

@Composable
fun ScheduleTimeTable(
    state: ParentScheduleState,
    events: List<ScheduleEvent>,
    onContentClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val innerPadding = 8.dp
        val bottomBuffer = 4.dp
        val extraSpacing = 16.dp + innerPadding + bottomBuffer
        val hourHeight = (maxHeight - extraSpacing) / 14

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                ScheduleTimeColumn(hourHeight = hourHeight)

                SchedulePlanner(
                    events = events,
                    state = state,
                    onContentClick = onContentClick,
                    hourHeight = hourHeight
                )
            }
        }
    }
}

@Composable
fun SchedulePlanner(
    events: List<ScheduleEvent>,
    state: ParentScheduleState,
    hourHeight: Dp,
    onContentClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    daysCount: Int = 7,
) {
    val slotHeight = hourHeight / 4
    val innerPadding = 4.dp
    val bottomBuffer = 4.dp
    val totalHeight = (hourHeight * 14) + (innerPadding * 2) + bottomBuffer

    val allBlocks = events.flatMap { event ->
        val indices = state.run { event.getIndices() }

        indices.flatMap { index ->
            event.toScheduleBlocks(index).map { block ->
                block to event
            }
        }
    }

    val uniqueBlocks = allBlocks
        .sortedBy { (_, event) -> if (event.isRecurring) 1 else 0 }
        .distinctBy { (block, _) ->
            "${block.dayIndex}-${block.startHour}-${block.startMinute}"
        }

    // 같은 이벤트 이면서 같은 요일인 조각 묶기
    val mergedBlocks = uniqueBlocks
        .groupBy { (block, event) -> "${event.id}-${block.dayIndex}" }
        .map { (_, group) ->
            val event = group.first().second
            val firstBlock = group.minByOrNull { it.first.startHour * 60 + it.first.startMinute }!!.first
            val totalDurationInSlots = group.sumOf { it.first.durationInSlots }

            Triple(firstBlock, totalDurationInSlots, event)
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
        if (mergedBlocks.isEmpty()) {
            KieroContentEmptyScreen()
        } else {
            mergedBlocks.forEach { (firstBlock, totalDuration, event) ->
                val hourOffset = hourHeight * (firstBlock.startHour - 8)
                val minuteOffset = slotHeight * (firstBlock.startMinute / 15)
                val topOffset = hourOffset + minuteOffset

                val blockHeight = slotHeight * totalDuration

                Box(
                    modifier = Modifier
                        .offset(x = dayWidth * firstBlock.dayIndex, y = topOffset)
                        .width(dayWidth)
                        .height(blockHeight)
                        .noRippleClickable(onClick = { onContentClick(event.id, event.date ?: "") })
                        .padding(horizontal = 3.dp)
                ) {
                    ScheduleEventBlock(block = firstBlock.copy(blockPosition = BlockPosition.SINGLE), modifier = Modifier.fillMaxSize())
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
                events = mockEvents,
                onContentClick = {_, _ -> }
            )
        }
    }
}
