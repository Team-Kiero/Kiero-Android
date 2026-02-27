package com.kiero.presentation.parent.screen.journey.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.divider.KieroDashedVerticalDivider
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.journey.model.TodayStatus

// Todo: 서버 기준으로 변경하기
@Composable
fun ParentJourneyTodayStatusItem(
    status: TodayStatus,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top
    ) {
        ParentJourneyLightDivider(
            status = status,
            modifier = Modifier.fillMaxHeight()
        )

        Spacer(modifier = Modifier.width(5.dp))

        ParentJourneyToday(
            status = status,
            modifier = Modifier
                .padding(bottom = 12.dp, end = 10.dp)
        )
    }
}

@Composable
private fun ParentJourneyToday(
    status: TodayStatus,
    modifier: Modifier = Modifier
) {
    val statusIcon = when(status) {
        TodayStatus.PAST_COMPLETED -> R.drawable.ic_check_circle_complete_outline
        TodayStatus.PAST_MISSED -> R.drawable.ic_miss_circle_outline
        TodayStatus.CURRENT_COMPLETED -> R.drawable.ic_parent_addschedule_check_on
        TodayStatus.UPCOMING -> R.drawable.ic_parent_addschedule_check_off
    }

    val statusTextColor = when(status) {
        TodayStatus.CURRENT_COMPLETED -> KieroTheme.colors.white
        TodayStatus.UPCOMING -> KieroTheme.colors.gray800
        else -> KieroTheme.colors.gray400
    }

    val statusDateColor = when(status) {
        TodayStatus.CURRENT_COMPLETED -> KieroTheme.colors.main
        TodayStatus.UPCOMING -> KieroTheme.colors.gray800
        else -> KieroTheme.colors.gray400
    }

    val hasImageStatus = when(status) {
        TodayStatus.PAST_COMPLETED, TodayStatus.CURRENT_COMPLETED -> true
        else -> false
    }

    Column (
        modifier = modifier
    ) {
        Text(
            text = "09:00 ~ 12:00",
            style = KieroTheme.typography.regular.body4,
            color = statusDateColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (status == TodayStatus.CURRENT_COMPLETED) KieroTheme.colors.schedule1.copy(0.1f) else KieroTheme.colors.black,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.dp,
                    color = status.getColor(KieroTheme.colors),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(statusIcon),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "키어로짱",
                style = KieroTheme.typography.regular.body4,
                color = statusTextColor
            )

            Spacer(modifier = Modifier.weight(1f))

            if (hasImageStatus) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_image),
                    tint = if (status == TodayStatus.CURRENT_COMPLETED) KieroTheme.colors.white else KieroTheme.colors.gray800,
                    contentDescription = null
                )

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                    tint = if (status == TodayStatus.CURRENT_COMPLETED) KieroTheme.colors.white else KieroTheme.colors.gray800,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
private fun ParentJourneyLightDivider(
    status : TodayStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlowingDot(
            coreColor = status.getDotColor(KieroTheme.colors),
            glowColor = status.getDotColor(KieroTheme.colors)
        )

        KieroDashedVerticalDivider(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun GlowingDot(
    modifier: Modifier = Modifier,
    coreColor: Color = KieroTheme.colors.schedule1,
    glowColor: Color = KieroTheme.colors.schedule1,
    dotSize: Dp = 7.dp,
    blurRadius: Dp = 3.dp
) {
    val totalSize = dotSize + (blurRadius * 4)

    Box(
        modifier = modifier.size(totalSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(dotSize + blurRadius)
                .blur(
                    radius = blurRadius,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .background(color = glowColor, shape = CircleShape)
        )

        Box(
            modifier = Modifier
                .size(dotSize)
                .background(color = coreColor, shape = CircleShape)
        )
    }
}

@Preview
@Composable
private fun ParentJourneyTodayStatusItemPreview() {
    KieroTheme {
        Column {
            ParentJourneyTodayStatusItem(
                status = TodayStatus.PAST_COMPLETED
            )

            ParentJourneyTodayStatusItem(
                status = TodayStatus.PAST_MISSED
            )

            ParentJourneyTodayStatusItem(
                status = TodayStatus.CURRENT_COMPLETED
            )

            ParentJourneyTodayStatusItem(
                status = TodayStatus.UPCOMING
            )
        }
    }
}
