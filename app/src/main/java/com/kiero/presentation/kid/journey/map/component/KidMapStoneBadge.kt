package com.kiero.presentation.kid.journey.map.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.journey.map.model.KidMapScheduleStatus
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType

@Composable
fun KidMapStoneBadge(
    stoneType: KidJourneyStoneType,
    status: KidMapScheduleStatus,
    modifier: Modifier = Modifier
) {
    val borderColor = when (status) {
        KidMapScheduleStatus.COMPLETE,
        KidMapScheduleStatus.VERIFIED -> KieroTheme.colors.main
        KidMapScheduleStatus.FAILED,
        KidMapScheduleStatus.SKIPPED -> KieroTheme.colors.point
        KidMapScheduleStatus.PENDING -> KieroTheme.colors.white
    }

    val statusLabel: Pair<String, Color>? = when (status) {
        KidMapScheduleStatus.COMPLETE,
        KidMapScheduleStatus.VERIFIED -> Pair("획득!", KieroTheme.colors.main)
        KidMapScheduleStatus.FAILED,
        KidMapScheduleStatus.SKIPPED -> Pair("실패!", KieroTheme.colors.point)
        else -> null
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .border(width = 1.dp, color = borderColor, shape = CircleShape)
                .then(
                    if (borderColor != KieroTheme.colors.white) {
                        Modifier.dropShadow(
                            shape = CircleShape,
                            shadow = Shadow(
                                radius = 3.dp,
                                spread = 0.dp,
                                color = borderColor,
                                offset = DpOffset(x = 0.dp, y = 0.dp)
                            )
                        )
                    } else Modifier
                )
                .background(color = KieroTheme.colors.gray900, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = stoneType.imageRes),
                contentDescription = stoneType.text,
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = statusLabel?.first ?: "획득!",
            color = (statusLabel?.second ?: KieroTheme.colors.main).copy(
                alpha = if (statusLabel != null) 1f else 0f
            ),
            style = KieroTheme.typography.regular.body6
        )
    }
}