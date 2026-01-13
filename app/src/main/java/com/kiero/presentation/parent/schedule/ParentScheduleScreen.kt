package com.kiero.presentation.parent.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.component.ParentFloatingButton
import com.kiero.presentation.parent.component.ParentTabRow
import com.kiero.presentation.parent.component.ParentUserSection
import com.kiero.presentation.parent.schedule.mission.ParentMissionScreen
import com.kiero.presentation.parent.schedule.plan.ParentPlanScreen

@Composable
fun ParentScheduleRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: () -> Unit,
) {
    ParentScheduleScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        navigateToScheduleAdd = navigateToScheduleAdd
    )
}

@Composable
private fun ParentScheduleScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ParentUserSection(
                userName = "근영맘",
                onUserNameClick = {},
                modifier = Modifier
                    .background(color = KieroTheme.colors.gray900)
            )

            ParentTabRow(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )

            when (selectedTabIndex) {
                0 -> ParentPlanScreen()
                1 -> ParentMissionScreen()
            }
        }

        ParentFloatingButton(
            buttonColor = KieroTheme.colors.white,
            onActiveClick = navigateToScheduleAdd,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 27.dp)
        )
    }
}

@Composable
@Preview
private fun ParentScheduleScreenPreview() {
    KieroTheme {
        ParentScheduleScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            navigateToScheduleAdd = {}
        )
    }
}