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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.journey.map.model.KidMapScheduleStatus
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType

private data class BadgeUiState(
    val borderColor: Color,
    val labelText: String,
    val labelColor: Color
)

@Composable
fun KidMapStoneBadge(
    stoneType: KidJourneyStoneType,
    status: KidMapScheduleStatus,
    modifier: Modifier = Modifier
) {
    val mainColor = KieroTheme.colors.main
    val pointColor = KieroTheme.colors.point
    val whiteColor = KieroTheme.colors.white

    val badgeStyle = remember(status, mainColor, pointColor, whiteColor) {
        when (status) {
            KidMapScheduleStatus.COMPLETED,
            KidMapScheduleStatus.VERIFIED -> BadgeUiState(
                borderColor = mainColor,
                labelText = "획득!",
                labelColor = mainColor
            )
            KidMapScheduleStatus.FAILED,
            KidMapScheduleStatus.SKIPPED -> BadgeUiState(
                borderColor = pointColor,
                labelText = "실패!",
                labelColor = pointColor
            )
            KidMapScheduleStatus.PENDING -> BadgeUiState(
                borderColor = whiteColor,
                labelText = "",
                labelColor = Color.Transparent
            )
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .border(width = 1.dp, color = badgeStyle.borderColor, shape = CircleShape)
                .then(
                    if (badgeStyle.borderColor != KieroTheme.colors.white) {
                        Modifier.dropShadow(
                            shape = CircleShape,
                            shadow = Shadow(
                                radius = 4.dp,
                                color = badgeStyle.borderColor,
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
            text = badgeStyle.labelText,
            color = badgeStyle.labelColor,
            style = KieroTheme.typography.regular.body6
        )
    }
}