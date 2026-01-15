package com.kiero.presentation.parent.schedule.mission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.component.model.Mission
import com.kiero.presentation.parent.schedule.mission.component.model.MissionLocalData
import com.kiero.presentation.parent.schedule.mission.component.model.MissionsByDate

@Composable
fun ParentMissionRoute(
) {
    val dummyData = MissionLocalData.fakeMissionEvent

    ParentMissionScreen(
        missionsByDateList = dummyData,
        onMissionClick = { missionId ->
            // TODO: 미션 클릭 처리
        }
    )
}

@Composable
fun ParentMissionScreen(
    missionsByDateList: List<MissionsByDate>,
    onMissionClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = missionsByDateList,
            key = { it.dueAt }
        ) { missionsByDate ->
            MissionDateGroup(
                missionsByDate = missionsByDate,
                onMissionClick = onMissionClick
            )
        }
    }
}

@Preview
@Composable
private fun ParentMissionScreenPreview() {
    KieroTheme {
        ParentMissionScreen(
            missionsByDateList = listOf(
                MissionsByDate(
                    dueAt = "2026-01-15",
                    dayOfWeek = "목요일",
                    missions = listOf(
                        Mission(1, "장보기", 50, false),
                        Mission(2, "설거지하기", 50, true)
                    )
                )
            ),
            onMissionClick = {}
        )
    }
}