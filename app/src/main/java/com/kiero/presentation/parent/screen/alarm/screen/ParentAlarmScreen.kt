package com.kiero.presentation.parent.screen.alarm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.emptyview.KieroEntireEmptyScreen
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.component.pulltorefresh.KieroPullToRefresh
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.presentation.parent.screen.alarm.component.ParentAlarmCard
import com.kiero.presentation.parent.screen.alarm.component.ParentAlarmDateHeader
import com.kiero.presentation.parent.screen.alarm.model.ParentAlarmUiModel
import com.kiero.presentation.parent.screen.alarm.state.AlarmFeedState
import com.kiero.presentation.parent.screen.alarm.viewmodel.ParentAlarmViewModel
import com.kiero.presentation.signup.parent.state.ParentSignUpSideEffect
import com.kiero.presentation.signup.parent.state.ParentSignUpState


@Composable
fun ParentAlarmRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSelection: () -> Unit,
    viewModel: ParentAlarmViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val refreshState = LocalRefreshState.current

    LaunchedEffect(Unit) {
        refreshState.refreshEvent.collect {
            listState.animateScrollToItem(0)
            viewModel.refresh(isRefresh = true)
        }
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is ParentSignUpSideEffect.NavigateToSelection -> navigateToSelection()
            else -> {}
        }
    }

    KieroPullToRefresh(
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.refresh(isRefresh = true) },
    ) {
        ParentAlarmScreen(
            state = state,
            authState = authState,
            navigateUp = navigateUp,
            onExpandClick = viewModel::toggleExpand,
            listState = listState,
            paddingValues = paddingValues,
            modifier = Modifier.fillMaxSize()
        )

        if (authState.isLoading) {
            KieroLoadingIndicator()
        }
    }
}

@Composable
private fun ParentAlarmScreen(
    state: AlarmFeedState,
    authState: ParentSignUpState,
    onExpandClick: (String) -> Unit,
    navigateUp: () -> Unit,
    listState: LazyListState,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        KieroTopbar(
            title = "알람",
            leftIconClick = navigateUp
        )

        when {
            state.isLoading -> {
                KieroLoadingIndicator()
            }

            state.alarms.isEmpty() -> {
                KieroEntireEmptyScreen(
                    text = "아직 아이로부터 도착한 알림이 없어요!",
                )
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        top = 15.dp,
                        bottom = 20.dp
                    )
                ) {
                    val sortedAlarms = state.alarms
                        .sortedWith(
                            compareByDescending<ParentAlarmUiModel> { it.date }
                                .thenByDescending { it.time }
                        )
                    val groupedAlarms = sortedAlarms.groupBy { it.date }

                    groupedAlarms.forEach { (date, alarmsForDate) ->
                        item(key = "header_$date") {
                            ParentAlarmDateHeader(date = date)
                        }

                        items(items = alarmsForDate, key = { it.id }) { alarm ->
                            ParentAlarmCard(
                                time = alarm.time,
                                message = alarm.message,
                                highlightTexts = listOf(alarm.highlightText),
                                highlightColor = alarm.highlightColor,
                                coinUsed = alarm.coinUsed,
                                imageUrl = alarm.imageUrl,
                                isExpanded = alarm.isExpanded,
                                onExpandClick = { onExpandClick(alarm.id) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
