package com.kiero.presentation.kid.journey.map.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.divider.KieroDashedHorizontalDivider
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.journey.map.model.KidMapItemUiModel
import com.kiero.presentation.kid.journey.map.model.KidMapScheduleStatus
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType

@Composable
fun KidMapListItem(
    item: KidMapItemUiModel,
    modifier: Modifier = Modifier
) {
    val nameColor = when {
        item.isOngoing || item.status == KidMapScheduleStatus.PENDING -> KieroTheme.colors.white
        else -> KieroTheme.colors.white.copy(alpha = 0.5f)
    }

    val timeColor = when {
        item.isOngoing || item.isNext -> KieroTheme.colors.main
        item.status == KidMapScheduleStatus.PENDING -> KieroTheme.colors.gray500
        else -> KieroTheme.colors.gray500.copy(alpha = 0.5f)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KidMapTimelineNode(
                    isOngoing = item.isOngoing,
                    isNext = item.isNext,
                    status = item.status
                )

                Text(
                    text = item.name,
                    color = nameColor,
                    style = KieroTheme.typography.bold.headLine3,
                    maxLines = 1,
                )

                if (item.isOngoing) {
                    KidMapOngoingBadge()
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${item.startTime} ~ ${item.endTime}",
                color = timeColor,
                style = KieroTheme.typography.regular.body4
            )
        }

        KieroDashedHorizontalDivider(
            color = KieroTheme.colors.gray600,
            thickness = 1.dp,
            modifier = Modifier
                .weight(0.25f)
                .padding(horizontal = 20.dp, vertical = 15.dp)
        )

        KidMapStoneBadge(
            stoneType = item.stoneType,
            status = item.status
        )
    }
}

@Preview
@Composable
private fun KidMapListItemPreview() {
    KieroTheme {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // 완료 - 라인 없음, 흐리게
            KidMapListItem(
                item = KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = KidJourneyStoneType.COURAGE,
                    status = KidMapScheduleStatus.COMPLETE
                )
            )
            // 실패 - 라인 없음, 흐리게
            KidMapListItem(
                item = KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = KidJourneyStoneType.COURAGE,
                    status = KidMapScheduleStatus.FAILED
                )
            )
            // 진행중 - 라인 있음, main 색상
            KidMapListItem(
                item = KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = true,
                    stoneType = KidJourneyStoneType.GRIT,
                    status = KidMapScheduleStatus.PENDING
                )
            )
            // 미래 PENDING - 라인 있음, white 색상
            KidMapListItem(
                item = KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = KidJourneyStoneType.WISDOM,
                    status = KidMapScheduleStatus.PENDING
                )
            )
        }
    }
}