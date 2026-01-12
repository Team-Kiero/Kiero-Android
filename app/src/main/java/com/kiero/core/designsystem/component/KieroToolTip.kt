package com.kiero.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroToolTip(
    message: String,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = KieroTheme.colors.gray900

    Box(
        modifier = modifier
            .padding(bottom = 13.dp)
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 3,
                    radius = 12.dp.toPx(),
                    centerX = size.width / 2,
                    centerY = size.height,
                    rounding = CornerRounding(radius = 2.dp.toPx())
                )

                val composePath = roundedPolygon.toPath().asComposePath()

                onDrawBehind {
                    val pivot = Offset(size.width / 2, size.height)

                    rotate(degrees = 90f, pivot = pivot) {
                        translate(left = 2.dp.toPx()) {
                            drawPath(path = composePath, color = backgroundColor)
                        }
                    }
                }
            }
            .background(color = backgroundColor, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 20.dp, vertical = 12.5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.gray300,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun KieroToolTipPreview() {
    KieroToolTip(
        message = "이 텍스트는 말풍선에 들어가는 텍스트"
    )
}