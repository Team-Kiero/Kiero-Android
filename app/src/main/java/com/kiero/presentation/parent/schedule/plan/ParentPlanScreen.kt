package com.kiero.presentation.parent.schedule.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.plan.component.plan.ScheduleDatebar
import com.kiero.presentation.parent.schedule.plan.component.plan.ScheduleTimeTable
import com.kiero.presentation.parent.schedule.plan.component.plan.ScheduleWeekTopbar
import com.kiero.presentation.parent.schedule.plan.state.ParentScheduleState
import com.kiero.presentation.parent.schedule.plan.state.toScheduleEvent

@Composable
fun ParentPlanScreen(
    state: ParentScheduleState,
    onDateChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val events = remember(state.planAllModel) {
        val recurring =
            state.planAllModel?.recurringSchedules?.map { it.toScheduleEvent() } ?: emptyList()
        val normal =
            state.planAllModel?.normalSchedules?.map { it.toScheduleEvent() } ?: emptyList()
        recurring + normal
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        ScheduleDatebar(
            date = state.dateRangeText,
            onPreviousClick = { onDateChange(false) },
            onNextClick = { onDateChange(true) }
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ScheduleWeekTopbar(
                    currentDate = state.currentDate
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                ScheduleTimeTable(
                    state = state,
                    events = events
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ParentPlanScreenPreview() {
    KieroTheme {
        ParentPlanScreen(
            state = ParentScheduleState(),
            onDateChange = {}
        )
    }
}