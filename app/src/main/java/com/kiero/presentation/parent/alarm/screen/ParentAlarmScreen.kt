package com.kiero.presentation.parent.alarm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.component.pulltorefresh.KieroPullToRefresh
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.presentation.main.navigation.ParentMainTab
import com.kiero.presentation.parent.alarm.component.ParentAlarmCard
import com.kiero.presentation.parent.alarm.component.ParentAlarmDateHeader
import com.kiero.presentation.parent.alarm.model.ParentAlarmUiModel
import com.kiero.presentation.parent.alarm.state.AlarmFeedState
import com.kiero.presentation.parent.alarm.viewmodel.ParentAlarmViewModel
import com.kiero.presentation.parent.component.ParentUserSection
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
        refreshState.refreshEvent.collect { tab ->
            if (tab == ParentMainTab.ALARM) {
                listState.animateScrollToItem(0)
                viewModel.refresh(isRefresh = true)
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
        onRefresh = {viewModel.refresh(isRefresh = true)},
    ) {
        ParentAlarmScreen(
            state = state,
            authState = authState,
            onExpandClick = viewModel::toggleExpand,
            onUserNameClick = viewModel::onProfileClick,
            listState = listState,
            paddingValues = paddingValues,
            modifier = Modifier.fillMaxSize()
        )

        if (authState.isLogoutDialogVisible) {
            KieroDialog(
                title = "로그아웃",
                subDescription = "로그아웃 하시겠습니까?",
                onDismiss = viewModel::onLogoutCancel,
                confirmAction = KieroConfirmAction(
                    text = "확인",
                    onClick = viewModel::onLogoutConfirm
                ),
                cancelAction = KieroCancelAction(
                    text = "취소",
                    onClick = viewModel::onLogoutCancel
                ),
                isDisabled = true,
                content = {}
            )
        }

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
    onUserNameClick: () -> Unit,
    listState: LazyListState,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        ParentUserSection(
            userName = authState.parentInfo.parentName,
            profileImage = authState.parentInfo.parentProfileImage,
            onUserNameClick = onUserNameClick,
            backgroundColor = KieroTheme.colors.black,
            modifier = Modifier
        )

        when {
            state.isLoading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = KieroTheme.colors.main)
                }
            }
            state.alarms.isEmpty() -> {
                EmptyAlarmView()
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

@Preview
@Composable
private fun EmptyAlarmView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(KieroTheme.colors.black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(175.dp))
        Text(
            text = "아직 아이로부터 도착한 알림이 없어요!",
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.gray400,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(id = R.drawable.img_parent_no_alarm),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(360f / 274f),
            contentScale = ContentScale.FillWidth
        )
    }
}