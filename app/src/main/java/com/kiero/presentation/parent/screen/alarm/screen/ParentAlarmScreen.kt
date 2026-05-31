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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.delay
import timber.log.Timber


@Composable
fun ParentAlarmRoute(
    targetId: Long?,
    shouldExpand: Boolean,
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSelection: () -> Unit,
    viewModel: ParentAlarmViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val refreshState = LocalRefreshState.current
    var hasScrolledToTarget by remember { mutableStateOf(false) }

    val isAtTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val buffer = 2
            totalItems > 0 && lastVisibleItemIndex >= totalItems - 1 - buffer
        }
    }

    LaunchedEffect(Unit) {
        refreshState.refreshEvent.collect {
            listState.animateScrollToItem(0)
            viewModel.refresh(isRefresh = true)
        }
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom && !state.isLoadingMore) {
            viewModel.loadMore()
        }
    }

    LaunchedEffect(state.alarms.size, targetId) {
        if (targetId == null || state.alarms.isEmpty() || hasScrolledToTarget) return@LaunchedEffect

        val sortedAlarms = state.alarms.sortedWith(
            compareByDescending<ParentAlarmUiModel> { it.date }.thenByDescending { it.time }
        )
        val groupedAlarms = sortedAlarms.groupBy { it.date }

        var targetIndex = -1
        var exactMatchedId = ""
        var currentIndex = 0

        for ((_, alarms) in groupedAlarms) {
            currentIndex++

            val itemIndex = alarms.indexOfFirst { it.id == targetId.toString() }

            if (itemIndex != -1) {
                targetIndex = currentIndex + itemIndex
                exactMatchedId = alarms[itemIndex].id
                break
            }
            currentIndex += alarms.size
        }

        if (targetIndex != -1) {
            if (shouldExpand) {
                viewModel.toggleExpand(exactMatchedId)
            }
            delay(300)

            try {
                listState.animateScrollToItem(index = targetIndex)
                hasScrolledToTarget = true
            } catch (e: Exception) {
                Timber.e(e, "FCM 타겟 자동 스크롤 실패")
            }
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
        enabled = isAtTop,
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
            .fillMaxSize()
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
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
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

                        itemsIndexed(
                            items = alarmsForDate,
                            key = { index, alarm -> "${alarm.id}_${index}" }
                        ) { index, alarm ->
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
