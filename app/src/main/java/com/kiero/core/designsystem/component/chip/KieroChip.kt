package com.kiero.core.designsystem.component.chip

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.chip.action.ChipAction
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroChip(
    action: ChipAction,
    modifier: Modifier = Modifier,
    horizontalPadding: Int = 12,
    verticalPadding: Int = 4,
    isCompleted : Boolean = false,
    isEnabled: Boolean = false,
) {
    val shape = RoundedCornerShape(36.dp)
    val backgroundColor = KieroTheme.colors.gray900

    val targetColor = when {
        !isEnabled -> KieroTheme.colors.gray500
        isCompleted -> KieroTheme.colors.main
        else -> KieroTheme.colors.gray100
    }

    val showGlow = isEnabled && isCompleted

    val infiniteTransition = rememberInfiniteTransition(label = "neonBreathing")

    val blurRadius by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "blurRadius"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glowAlpha"
    )

    Row(
        modifier = modifier
            .drawBehind {
                val cornerRadius = shape.topStart.toPx(size, this)

                if (showGlow) {
                    val paint = Paint().asFrameworkPaint().apply {
                        color = targetColor.copy(alpha = glowAlpha).toArgb()
                        style = android.graphics.Paint.Style.STROKE
                        strokeWidth = 3.dp.toPx()
                        strokeCap = android.graphics.Paint.Cap.ROUND
                        isAntiAlias = true

                        BlurMaskFilter(
                            blurRadius,
                            BlurMaskFilter.Blur.NORMAL
                        )
                    }

                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawRoundRect(
                            0f, 0f, size.width, size.height,
                            cornerRadius, cornerRadius,
                            paint
                        )
                    }
                }

                drawRoundRect(
                    color = backgroundColor,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Fill
                )

                drawRoundRect(
                    color = targetColor,
                    style = Stroke(width = 1.dp.toPx()),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
            .padding(
                horizontal = horizontalPadding.dp,
                vertical = verticalPadding.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        action(
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun KieroChipPreview() {
    KieroTheme {
        Column {
            KieroChip(
                isCompleted = false,
                isEnabled = true,
                action = KieroCoinAction(
                    coinCount = 150,
                    isCompleted = true,
                    isEnabled = true,
                    onClick = {}
                )
            )

            KieroChip(
                isCompleted = true,
                isEnabled = true,
                action = KieroCoinAction(
                    coinCount = 150,
                    isCompleted = true,
                    isEnabled = true,
                    onClick = {}
                )
            )
        }
    }
}
