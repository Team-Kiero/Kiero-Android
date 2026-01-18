package com.kiero.presentation.parent.alarm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.alarm.component.ParentAlarmCard
import com.kiero.presentation.parent.alarm.component.ParentAlarmDateHeader
import com.kiero.presentation.parent.alarm.model.ParentAlarmUiModel
import com.kiero.presentation.parent.alarm.state.AlarmFeedState
import com.kiero.presentation.parent.alarm.viewmodel.ParentAlarmViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.collections.sortedWith


@Composable
fun ParentAlarmRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentAlarmViewModel = hiltViewModel(),
    isTabReselected: Boolean = false
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(isTabReselected) {
        if (isTabReselected) {
            listState.animateScrollToItem(0)
            viewModel.refresh(1)
        }
    }

    ParentAlarmScreen(
        state = state,
        onExpandClick = viewModel::toggleExpand,
        listState = listState,
        paddingValues = paddingValues,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ParentAlarmScreen(
    state: AlarmFeedState,
    onExpandClick: (String) -> Unit,
    listState: LazyListState,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        // TODO: PR #31 머지 후 공통 ParentUserSection으로 헤더 교체
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = KieroTheme.colors.black)
                .statusBarsPadding()
                .padding(
                    top = 25.dp,
                    bottom = 15.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "근영맘",
                style = KieroTheme.typography.regular.body2,
                color = KieroTheme.colors.white
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_profile),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = KieroTheme.colors.gray500
            )
        }

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = KieroTheme.colors.main)
                }
            }
            state.alarms.isEmpty() ->{
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
                        .sortedWith(compareByDescending<ParentAlarmUiModel> { it.date }
                            .thenByDescending { it.time })
                    val groupedAlarms = sortedAlarms.groupBy { it.date }

                    groupedAlarms.forEach { (date, alarmsForDate) ->
                        item(key = "header_$date") { ParentAlarmDateHeader(date = date) }

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
        modifier = Modifier.fillMaxWidth().fillMaxHeight().background(KieroTheme.colors.black),
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
            modifier = Modifier.fillMaxWidth().aspectRatio(360f / 274f),
            contentScale = ContentScale.FillWidth
        )
    }
}