package com.kiero.core.designsystem.component.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroDashedVerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = KieroTheme.colors.gray400,
    endColor: Color = KieroTheme.colors.gray600,
    thickness: Dp = 1.dp,
    dashWidth: Dp = 2.dp,
    dashGap: Dp = 3.dp,
) {
    Canvas(
        modifier = modifier
            .fillMaxHeight()
            .width(thickness)
    ) {
        val dashPx = dashWidth.toPx()
        val gapPx = dashGap.toPx()
        val strokeWidthPx = size.width
        val capRadius = strokeWidthPx / 2f

        var y = capRadius

        val maxHeight = size.height - capRadius

        val gradientBrush = Brush.verticalGradient(
            colors = listOf(color, endColor),
            startY = 0f,
            endY = size.height
        )

        while (y < maxHeight) {
            drawLine(
                brush = gradientBrush,
                start = Offset(strokeWidthPx / 2, y),
                end = Offset(strokeWidthPx / 2, (y + dashPx).coerceAtMost(maxHeight)),
                strokeWidth = strokeWidthPx,
                cap = StrokeCap.Round
            )
            y += dashPx + gapPx
        }
    }
}

@Preview
@Composable
private fun KieroDashedVerticalDividerPreview() {
    KieroTheme {
        KieroDashedVerticalDivider(
            thickness = 2.dp
        )
    }
}
