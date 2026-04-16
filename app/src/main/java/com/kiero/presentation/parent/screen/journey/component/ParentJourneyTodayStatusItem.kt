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
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.divider.KieroDashedVerticalDivider
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.journey.model.TodayJourneyUiModel
import com.kiero.presentation.parent.screen.journey.model.TodayStatus

@Composable
fun ParentJourneyTodayStatusItem(
    item: TodayJourneyUiModel,
    modifier: Modifier = Modifier,
    onItemClick: (TodayJourneyUiModel) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top
    ) {
        ParentJourneyLightDivider(
            status = item.todayStatus,
            modifier = Modifier.then(
                if (item.todayStatus == TodayStatus.TODAY_COMPLETED) Modifier.fillMaxHeight()
                else Modifier
            )
        )

        Spacer(modifier = Modifier.width(5.dp))

        ParentJourneyToday(
            item = item,
            modifier = Modifier.padding(bottom = 12.dp, end = 10.dp),
            onItemClick = if (item.isAuthenticated) {
                { onItemClick(item) }
            } else null
        )
    }
}

@Composable
private fun ParentJourneyToday(
    item: TodayJourneyUiModel,
    modifier: Modifier = Modifier,
    onItemClick: (() -> Unit)? = null
) {
    val isUpcoming = item.todayStatus == TodayStatus.UPCOMING

    val statusIcon = when (item.todayStatus) {
        TodayStatus.PAST_COMPLETED -> R.drawable.ic_check_circle_complete_outline
        TodayStatus.PAST_MISSED -> R.drawable.ic_miss_circle_outline
        TodayStatus.CURRENT_COMPLETED -> {
            if (item.isAuthenticated) R.drawable.ic_parent_addschedule_check_on
            else R.drawable.ic_parent_addschedule_check_off
        }
        TodayStatus.NEXT_UPCOMING -> R.drawable.ic_parent_addschedule_check_off
        TodayStatus.UPCOMING -> R.drawable.ic_parent_addschedule_check_off
        TodayStatus.TODAY_COMPLETED -> null
    }

    val statusTextColor = when {
        item.todayStatus == TodayStatus.CURRENT_COMPLETED -> KieroTheme.colors.main
        item.todayStatus == TodayStatus.NEXT_UPCOMING -> KieroTheme.colors.main
        isUpcoming -> KieroTheme.colors.gray800
        else -> KieroTheme.colors.gray400
    }

    val statusDateColor = when {
        item.todayStatus == TodayStatus.CURRENT_COMPLETED -> KieroTheme.colors.main
        item.todayStatus == TodayStatus.NEXT_UPCOMING -> KieroTheme.colors.main
        isUpcoming -> KieroTheme.colors.gray800
        else -> KieroTheme.colors.gray400
    }

    val cardBackground = when {
        item.todayStatus == TodayStatus.CURRENT_COMPLETED && item.isAuthenticated ->
            KieroTheme.colors.schedule1.copy(alpha = 0.1f)
        else -> KieroTheme.colors.black
    }

    val showImageIcon = when (item.todayStatus) {
        TodayStatus.PAST_COMPLETED -> item.isAuthenticated
        TodayStatus.CURRENT_COMPLETED -> item.isAuthenticated
        else -> false
    }

    val imageIconTint = if (item.todayStatus == TodayStatus.CURRENT_COMPLETED && item.isAuthenticated)
        KieroTheme.colors.white
    else
        KieroTheme.colors.gray800

    Column(
        modifier = modifier
    ) {
        if (item.todayStatus != TodayStatus.TODAY_COMPLETED) {
            if (item.scheduleLabel.isNotEmpty()) {
                Text(
                    text = item.scheduleLabel,
                    style = KieroTheme.typography.regular.body6,
                    color = if (item.isOngoing) KieroTheme.colors.gray600 else KieroTheme.colors.gray400
                )
            }

            Text(
                text = item.date,
                style = KieroTheme.typography.regular.body4,
                color = statusDateColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = cardBackground, shape = RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = item.todayStatus.getColor(KieroTheme.colors),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .then(
                        if (onItemClick != null) {
                            Modifier.noRippleClickable(
                                onClick = onItemClick
                            )
                        } else Modifier
                    )
                    .padding(horizontal = 10.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (statusIcon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(statusIcon),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.todayMission,
                    style = KieroTheme.typography.regular.body4,
                    color = statusTextColor
                )

                Spacer(modifier = Modifier.weight(1f))

                if (showImageIcon) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_image),
                        tint = imageIconTint,
                        contentDescription = null
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                        tint = imageIconTint,
                        contentDescription = null
                    )
                }
            }
        } else {
            Text(
                text = "오늘 일정이 모두 완료되었어요.",
                style = KieroTheme.typography.regular.body4,
                color = statusTextColor
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
private fun ParentJourneyLightDivider(
    status: TodayStatus,
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

        when (status) {
            TodayStatus.UPCOMING -> {
                KieroDashedVerticalDivider(
                    modifier = Modifier.weight(1f),
                    color = KieroTheme.colors.gray800,
                    endColor = KieroTheme.colors.black
                )
            }
            TodayStatus.NEXT_UPCOMING -> {
                KieroDashedVerticalDivider(modifier = Modifier.weight(1f))
            }
            TodayStatus.TODAY_COMPLETED -> { /* 라인 없음 */ }
            else -> {
                KieroDashedVerticalDivider(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun GlowingDot(
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
                .blur(radius = blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded)
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
            // Case2: 지난 일정 + 인증완료 => Completed
            ParentJourneyTodayStatusItem(
                item = TodayJourneyUiModel(
                    date = "09:00-14:00",
                    todayStatus = TodayStatus.PAST_COMPLETED,
                    todayMission = "학교 수업",
                    isAuthenticated = true
                )
            )
            // Case3: 지난 일정 + 미인증 => Failed
            ParentJourneyTodayStatusItem(
                item = TodayJourneyUiModel(
                    date = "09:00-14:00",
                    todayStatus = TodayStatus.PAST_MISSED,
                    todayMission = "학교 수업",
                    isAuthenticated = false
                )
            )
            // Case1-c: 현재 일정 + 미인증 => pendding
            ParentJourneyTodayStatusItem(
                item = TodayJourneyUiModel(
                    date = "13:00-14:00",
                    todayStatus = TodayStatus.CURRENT_COMPLETED,
                    todayMission = "학교 수업",
                    isAuthenticated = false
                )
            )
            // Case1-d: 현재 일정 + 인증함 => verified
            ParentJourneyTodayStatusItem(
                item = TodayJourneyUiModel(
                    date = "13:00-14:00",
                    todayStatus = TodayStatus.CURRENT_COMPLETED,
                    todayMission = "학교 수업",
                    isAuthenticated = true
                )
            )
            // Case4: 대기 상태 => panding
            ParentJourneyTodayStatusItem(
                item = TodayJourneyUiModel(
                    date = "09:00-14:00",
                    todayStatus = TodayStatus.UPCOMING,
                    todayMission = "학교 수업",
                    isAuthenticated = false
                )
            )
            // Case5: 하루 마무리 => isFireLit
            ParentJourneyTodayStatusItem(
                item = TodayJourneyUiModel(
                    todayStatus = TodayStatus.TODAY_COMPLETED,
                    todayMission = ""
                )
            )
        }
    }
}
