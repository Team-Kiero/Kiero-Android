package com.kiero.presentation.parent.schedule.mission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.component.model.Mission
import com.kiero.presentation.parent.schedule.mission.component.model.MissionsByDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ParentMissionRoute(
) {
    val dummyData = MissionsByDate.fakeMissionEvent

    ParentMissionScreen(
        missionsByDateList = dummyData,
        onMissionClick = { missionId ->
            // TODO: 미션 클릭 처리
        }
    )
}

@Composable
fun ParentMissionScreen(
    missionsByDateList: ImmutableList<MissionsByDate>,
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
        itemsIndexed(
            items = missionsByDateList,
            key = { index, missionsByDate ->
                "${missionsByDate.dueAt}_$index"
            }
        ) { index, missionsByDate ->
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
            missionsByDateList = persistentListOf(
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