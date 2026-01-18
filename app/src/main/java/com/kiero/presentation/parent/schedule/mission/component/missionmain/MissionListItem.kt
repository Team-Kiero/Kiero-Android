package com.kiero.presentation.parent.schedule.mission.component.missionmain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun MissionListItem(
    missionTitle: String,
    reward: Int,
    onMissionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onMissionClick)
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = 16.dp,
                vertical = 20.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = missionTitle,
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.white
        )

        Spacer(modifier = Modifier.weight(1f))

        /*
            TODO : pull 받고 수정
         */
        KieroChip(
            action = KieroCoinAction(
                coinCount = reward,
                isEnabled = true,
                onClick = {}
            )
        )
    }
}

@Composable
fun MissionInfo(
    dayOfWeek: String,
    dueAt: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Unspecified),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = dayOfWeek,
            color = KieroTheme.colors.gray300,
            style = KieroTheme.typography.semiBold.title4
        )

        Text(
            text = dueAt,
            color = KieroTheme.colors.gray500,
            style = KieroTheme.typography.regular.body3
        )
    }
}

@Preview
@Composable
private fun MissionListItemPreview() {
    KieroTheme {
        Column {
            MissionListItem(
                missionTitle = "테스트 미션",
                reward = 150,
                onMissionClick = {}
            )

            MissionInfo(
                dayOfWeek = "월요일",
                dueAt = "2023.05.01"
            )
        }
    }
}