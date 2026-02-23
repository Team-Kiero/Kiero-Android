package com.kiero.presentation.parent.mission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.mission.model.KidMissionListUiModel
import com.kiero.presentation.parent.mission.component.missionmain.MissionListItem


@Composable
fun MissionDateGroup(
    missionsByDate: KidMissionListUiModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        missionsByDate.missions.forEach { mission ->
            MissionListItem(
                missionTitle = mission.name,
                reward = mission.reward,
            )
        }
    }
}

@Preview
@Composable
private fun MissionDateGroupPreview() {
    KieroTheme {


    }
}