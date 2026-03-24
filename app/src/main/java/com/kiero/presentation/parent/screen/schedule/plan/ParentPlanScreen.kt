package com.kiero.presentation.parent.screen.schedule.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.RefreshState
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.data.parent.plan.model.toUiModel
import com.kiero.presentation.main.navigation.ParentMainTab
import com.kiero.presentation.parent.screen.schedule.plan.component.plan.ScheduleDatebar
import com.kiero.presentation.parent.screen.schedule.plan.component.plan.ScheduleTimeTable
import com.kiero.presentation.parent.screen.schedule.plan.component.plan.ScheduleWeekTopbar
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleState

@Composable
fun ParentPlanScreen(
    state: ParentScheduleState,
    onResetToday: () -> Unit,
    onContentClick: (String, String) -> Unit,
    onDateChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = LocalRefreshState.current
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        refreshState.refreshEvent.collect { tab ->
            if (tab == ParentMainTab.SCHEDULE) {
                onResetToday()
                listState.animateScrollToItem(0)
            }
        }

    }
    val events = remember(state.planAllModel) {
        state.planAllModel?.let { model ->
            buildList {
                addAll(model.recurringSchedules.filter { it.scheduleStatus != "SKIPPED" }.map { it.toUiModel() })
                addAll(model.normalSchedules.filter { it.scheduleStatus != "SKIPPED" }.map { it.toUiModel() })
            }
        } ?: emptyList()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ScheduleDatebar(
            date = state.dateRangeText,
            onPreviousClick = { if (state.canGoPrevious) onDateChange(false) },
            onNextClick = { if (state.canGoNext) onDateChange(true) },
            isPreviousEnabled = state.canGoPrevious,
            isNextEnabled = state.canGoNext,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ScheduleWeekTopbar(
                    currentDate = state.currentDate
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                ScheduleTimeTable(
                    state = state,
                    events = events,
                    onContentClick = onContentClick,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ParentPlanScreenPreview() {
    KieroTheme {
        CompositionLocalProvider(
            LocalRefreshState provides RefreshState()
        ) {
            ParentPlanScreen(
                state = ParentScheduleState(),
                onResetToday = {},
                onContentClick = { _, _ -> },
                onDateChange = {}
            )
        }
    }
}