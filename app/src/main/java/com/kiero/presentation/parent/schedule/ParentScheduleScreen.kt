package com.kiero.presentation.parent.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.component.MissionTabFab
import com.kiero.presentation.parent.component.ParentTabRow
import com.kiero.presentation.parent.component.ParentUserSection
import com.kiero.presentation.parent.component.PlanTabFab
import com.kiero.presentation.parent.schedule.mission.ParentMissionRoute
import com.kiero.presentation.parent.schedule.model.TabItem
import com.kiero.presentation.parent.schedule.plan.ParentPlanScreen
import com.kiero.presentation.parent.schedule.plan.model.ScheduleData

@Composable
fun ParentScheduleRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: () -> Unit,
    navigateToMissionAdd: () -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    ParentScheduleScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        navigateToScheduleAdd = navigateToScheduleAdd,
        navigateToMissionAdd = navigateToMissionAdd,
        selectedTabIndex = selectedTabIndex,
        onTabSelected = { selectedTabIndex = it }
    )
}

@Composable
private fun ParentScheduleScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: () -> Unit,
    navigateToMissionAdd: () -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = remember { TabItem.entries.map { it.title } }
    var isMissionFabExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(color = KieroTheme.colors.black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ParentUserSection(
                userName = "근영맘",
                onUserNameClick = {},
                modifier = Modifier
                    .background(color = KieroTheme.colors.gray900)
            )

            ParentTabRow(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = onTabSelected
            )

            when (selectedTabIndex) {
                0 -> ParentPlanScreen(events = ScheduleData.fakeScheduleEvents)
                1 -> ParentMissionRoute()
            }
        }

        if (selectedTabIndex == 1 && isMissionFabExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isMissionFabExpanded = false
                    }
            )
        }

        when (selectedTabIndex) {
            0 -> PlanTabFab(
                onScheduleAdd = { navigateToScheduleAdd() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )

            1 -> MissionTabFab(
                isExpanded = isMissionFabExpanded,
                onExpandedChange = { isMissionFabExpanded = it },
                onMissionAdd = { navigateToMissionAdd() },
                onMissionRecommend = { /* 규현이 화면 */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end =16.dp, bottom = 52.dp)
            )
        }
    }
}

@Composable
@Preview
private fun ParentScheduleScreenPreview() {
    KieroTheme {
        ParentScheduleScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            navigateToScheduleAdd = {},
            navigateToMissionAdd = {},
            selectedTabIndex = 0,
            onTabSelected = {}
        )
    }
}