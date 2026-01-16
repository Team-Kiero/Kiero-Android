package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentMissionEditForm(
    missionName: String,
    dueDate: String,
    reward: Int,
    onNameChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onRewardChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(
                top = 12.dp,
                end = 14.dp,
                bottom = 12.dp,
                start = 14.dp
            ),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // TODO: PR #35 머지 후 ScheduleTextField로 교체
        Text(
            text = "미션 이름: $missionName",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.regular.body2
        )

        // TODO: PR #35 머지 후 MissionCalendar 컴포넌트로 교체
        Text(
            text = "마감일",
            color = KieroTheme.colors.gray200,
            style = KieroTheme.typography.regular.body3
        )
        Text(
            text = dueDate,
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title3
        )

        // TODO: PR #35 머지 후 MissionAwardInfo + MissionAwardSelect로 교체
        Text(
            text = "보상",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.regular.body2
        )
        Text(
            text = "$reward 개",
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title3
        )
    }
}

@Preview
@Composable
private fun ParentMissionEditFormPreview() {
    KieroTheme {
        ParentMissionEditForm(
            missionName = "독서록 챙기기",
            dueDate = "2025.12.26.(금)",
            reward = 20,
            onNameChange = {},
            onDateClick = {},
            onRewardChange = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}