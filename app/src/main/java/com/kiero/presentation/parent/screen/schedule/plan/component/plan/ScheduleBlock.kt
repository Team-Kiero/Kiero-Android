package com.kiero.presentation.parent.screen.schedule.plan.component.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.schedule.model.BlockPosition
import com.kiero.presentation.parent.screen.schedule.model.ScheduleBlock

@Composable
fun ScheduleEventBlock(
    block: ScheduleBlock,
    modifier: Modifier = Modifier,
) {
    val slotHeight = 9.5.dp
    val totalHeight = slotHeight * block.durationInSlots
    val duration = block.durationInSlots

    val shape = when (block.blockPosition) {
        BlockPosition.SINGLE -> RoundedCornerShape(8.dp)
        BlockPosition.TOP -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
        BlockPosition.MIDDLE -> RoundedCornerShape(0.dp)
        BlockPosition.BOTTOM -> RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHeight)
            .clip(shape)
            .background(block.color.copy(alpha = 0.2f))
    ) {
        val (maxLines, textToDisplay) = when (duration > 1) {
            true -> {
                2 to block.title
            }
            else -> {
                1 to block.title
            }
        }

        if (block.title.isNotEmpty() && duration >= 1) {
            Column(modifier = Modifier.fillMaxSize()) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 5.dp,
                    color = block.color
                )

                if (duration > 1) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .size(5.dp)
                                .drawBehind {
                                    drawIntoCanvas { canvas ->
                                        val paint = Paint()
                                        val frameworkPaint = paint.asFrameworkPaint()

                                        frameworkPaint.color =
                                            block.color.copy(alpha = 0.5f).toArgb()
                                        frameworkPaint.setShadowLayer(
                                            4.dp.toPx(),
                                            0.dp.toPx(),
                                            0.dp.toPx(),
                                            block.color.copy(alpha = 0.5f).toArgb()
                                        )

                                        canvas.drawCircle(
                                            center = Offset(size.width / 2, size.height / 2),
                                            radius = size.width / 2,
                                            paint = paint
                                        )
                                    }
                                }
                                .clip(CircleShape)
                                .background(block.color.copy(0.5f))
                        )

                        Spacer(modifier = Modifier.width(3.dp))

                        Text(
                            text = textToDisplay,
                            color = KieroTheme.colors.white,
                            textAlign = TextAlign.Start,
                            style = KieroTheme.typography.regular.body5.copy(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                ),
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Bottom,
                                    trim = LineHeightStyle.Trim.None
                                )
                            ),
                            maxLines = maxLines,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        } else if (block.title.isNotEmpty() && duration < 1) {
            Column(modifier = Modifier.fillMaxSize()) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 5.dp,
                    color = block.color
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2D2F34)
@Composable
private fun ScheduleEventBlockPreview() {
    KieroTheme {

        ScheduleEventBlock(
            block = ScheduleBlock(
                id = "1",
                title = "수영입수",
                color = Color(0xFF4A5F7A),
                startHour = 16,
                startMinute = 0,
                durationInSlots = 2,
                dayIndex = 0,
                blockPosition = BlockPosition.SINGLE
            )
        )

    }
}