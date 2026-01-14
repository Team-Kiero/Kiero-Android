package com.kiero.presentation.parent.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.component.ParentFloatingButton
import com.kiero.presentation.parent.component.ParentTabRow
import com.kiero.presentation.parent.component.ParentUserSection
import com.kiero.presentation.parent.schedule.mission.ParentMissionScreen
import com.kiero.presentation.parent.schedule.model.TabItem
import com.kiero.presentation.parent.schedule.plan.ParentPlanScreen
import com.kiero.presentation.parent.schedule.plan.model.ScheduleData

@Composable
fun ParentScheduleRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: () -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    ParentScheduleScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        navigateToScheduleAdd = navigateToScheduleAdd,
        selectedTabIndex = selectedTabIndex,
        onTabSelected = { selectedTabIndex = it }
    )
}

@Composable
private fun ParentScheduleScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: () -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = remember { TabItem.entries.map { it.title } }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
        containerColor = KieroTheme.colors.black,
        floatingActionButton = {
            ParentFloatingButton(
                buttonColor = KieroTheme.colors.white,
                onActiveClick = navigateToScheduleAdd,
                modifier = Modifier
                    .padding(bottom = 24.dp, end = 27.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                1 -> ParentMissionScreen()
            }
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
            selectedTabIndex = 0,
            onTabSelected = {}
        )
    }
}