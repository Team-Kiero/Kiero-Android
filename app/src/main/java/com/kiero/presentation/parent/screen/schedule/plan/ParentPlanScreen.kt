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
import com.kiero.presentation.parent.screen.schedule.model.ScheduleEvent
import com.kiero.presentation.parent.screen.schedule.plan.component.plan.ScheduleDatebar
import com.kiero.presentation.parent.screen.schedule.plan.component.plan.ScheduleTimeTable
import com.kiero.presentation.parent.screen.schedule.plan.component.plan.ScheduleWeekTopbar
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

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

    val events = remember(
        state.planAllModel,
        state.currentDate,
        state.hiddenNormalScheduleKeys,
        state.hiddenRecurringScheduleIds,
        state.hiddenRecurringOccurrenceKeys,
    ) {
        state.planAllModel?.let { model ->
            val currentWeekMonday =
                state.currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

            buildList<ScheduleEvent> {
                model.recurringSchedules
                    .filter { it.scheduleStatus != "SKIPPED" }
                    .forEach { recurring ->
                        if (state.hiddenRecurringScheduleIds.contains(recurring.scheduleId)) {
                            return@forEach
                        }

                        val baseEvent = recurring.toUiModel()

                        val repeatStartDate = runCatching {
                            LocalDate.parse(recurring.repeatStartDate.take(10))
                        }.getOrNull() ?: LocalDate.MIN

                        val dayCodes = recurring.dayOfWeek
                            .split(",")
                            .map { it.trim().uppercase() }
                            .filter { it.isNotBlank() }

                        dayCodes.forEach { dayCode ->
                            val dayIndex = dayCode.toDayIndex()
                            val occurrenceDate = currentWeekMonday.plusDays(dayIndex.toLong())

                            if (occurrenceDate.isBefore(repeatStartDate)) return@forEach

                            val hiddenKey = ParentScheduleState.recurringOccurrenceKey(
                                recurring.scheduleId,
                                occurrenceDate
                            )

                            if (state.hiddenRecurringOccurrenceKeys.contains(hiddenKey)) {
                                return@forEach
                            }

                            add(
                                baseEvent.copy(
                                    dayOfWeek = dayCode,
                                    date = occurrenceDate.toString(),
                                )
                            )
                        }
                    }

                model.normalSchedules
                    .filter { it.scheduleStatus != "SKIPPED" }
                    .forEach { normal ->
                        val normalDate = runCatching {
                            LocalDate.parse(normal.date.take(10))
                        }.getOrNull() ?: return@forEach

                        val hiddenKey = ParentScheduleState.normalScheduleKey(
                            normal.scheduleId,
                            normalDate
                        )

                        if (state.hiddenNormalScheduleKeys.contains(hiddenKey)) {
                            return@forEach
                        }

                        add(
                            normal.toUiModel().copy(
                                date = normal.date.take(10)
                            )
                        )
                    }
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

        ScheduleWeekTopbar(
            currentDate = state.currentDate
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ScheduleTimeTable(
                    state = state,
                    events = events,
                    onContentClick = onContentClick,
                )
            }

            item {
                Spacer(modifier = Modifier.height(83.dp))
            }
        }
    }
}

private fun String.toDayIndex(): Int {
    return when (this.uppercase()) {
        "MON" -> 0
        "TUE" -> 1
        "WED" -> 2
        "THU" -> 3
        "FRI" -> 4
        "SAT" -> 5
        "SUN" -> 6
        else -> 0
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