package com.kiero.presentation.parent.schedule.mission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.component.missionmain.MissionInfo
import com.kiero.presentation.parent.schedule.mission.component.missionmain.MissionListItem
import com.kiero.presentation.parent.schedule.mission.component.model.Mission
import com.kiero.presentation.parent.schedule.mission.component.model.MissionsByDate


@Composable
fun MissionDateGroup(
    missionsByDate: MissionsByDate,
    onMissionClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MissionInfo(
            dayOfWeek = missionsByDate.dayOfWeek,
            dueAt = missionsByDate.dueAt
        )

        missionsByDate.missions.forEach { mission ->
            MissionListItem(
                missionTitle = mission.name,
                reward = mission.reward,
                onMissionClick = { onMissionClick(mission.id) }
            )
        }
    }
}

@Preview
@Composable
private fun MissionDateGroupPreview() {
    KieroTheme {
        MissionDateGroup(
            missionsByDate = MissionsByDate(
                dueAt = "2026-01-15",
                dayOfWeek = "목요일",
                missions = listOf(
                    Mission(
                        id = 1,
                        name = "장보기",
                        reward = 50,
                        isCompleted = false
                    ),
                    Mission(
                        id = 2,
                        name = "설거지하기",
                        reward = 50,
                        isCompleted = true
                    )
                )
            ),
            onMissionClick = {}
        )
    }
}