package com.kiero.presentation.kid.journey.map.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.journey.map.model.KidMapScheduleStatus

@Composable
fun KidMapTimelineNode(
    isOngoing: Boolean,
    status: KidMapScheduleStatus,
    modifier: Modifier = Modifier,
    isNext: Boolean = false
) {
    val showLine = isOngoing || isNext || status == KidMapScheduleStatus.PENDING

    val iconColor = when {
        isOngoing || isNext -> KieroTheme.colors.main
        status == KidMapScheduleStatus.PENDING -> KieroTheme.colors.white
        else -> KieroTheme.colors.white.copy(alpha = 0.5f)
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (showLine) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_kid_map_line),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .height(0.dp)
                    .wrapContentHeight(unbounded = true, align = Alignment.Bottom)
                    .align(Alignment.TopCenter)
                    .offset(y = 4.dp)
            )
        }

        val starShape = GenericShape { size, _ ->
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f
            val cp = 0.1f

            moveTo(cx, 0f)
            cubicTo(cx + w * cp, cy - h * cp, cx + w * cp, cy - h * cp, w, cy)
            cubicTo(cx + w * cp, cy + h * cp, cx + w * cp, cy + h * cp, cx, h)
            cubicTo(cx - w * cp, cy + h * cp, cx - w * cp, cy + h * cp, 0f, cy)
            cubicTo(cx - w * cp, cy - h * cp, cx - w * cp, cy - h * cp, cx, 0f)
            close()
        }

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_kid_map_star),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(12.dp)
                .then(
                    if (isOngoing || isNext) {
                        Modifier.dropShadow(
                            shape = starShape,
                            shadow = Shadow(
                                radius = 3.dp,
                                color = KieroTheme.colors.main,
                            )
                        )
                    } else Modifier
                )
        )
    }
}