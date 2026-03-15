package com.kiero.core.designsystem.component.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun KieroDashedHorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = KieroTheme.colors.gray400,
    endColor: Color = KieroTheme.colors.gray600,
    thickness: Dp = 1.dp,
    dashWidth: Dp = 2.dp,
    dashGap: Dp = 3.dp,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        val dashPx = dashWidth.toPx()
        val gapPx = dashGap.toPx()
        val strokeWidthPx = size.height
        val capRadius = strokeWidthPx / 2f

        var x = capRadius

        val maxWidth = size.width - capRadius

        val gradientBrush = Brush.horizontalGradient(
            colors = listOf(color, endColor),
            startX = 0f,
            endX = size.width
        )

        while (x < maxWidth) {
            drawLine(
                brush = gradientBrush,
                start = Offset(x, strokeWidthPx / 2),
                end = Offset((x + dashPx).coerceAtMost(maxWidth), strokeWidthPx / 2),
                strokeWidth = strokeWidthPx,
                cap = StrokeCap.Round
            )
            x += dashPx + gapPx
        }
    }
}

@Preview
@Composable
private fun KieroDashedHorizontalDividerPreview() {
    KieroTheme {
        KieroDashedHorizontalDivider(
            thickness = 2.dp
        )
    }
}