package com.kiero.presentation.parent.alarm.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.kiero.presentation.parent.alarm.viewmodel.AlarmFeedViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ParentAlarmRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: AlarmFeedViewModel = hiltViewModel(),
    isTabReselected: Boolean = false
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val childId = 1L

    // GNB 재클릭 시: 최상단 이동 및 새로고침 (명세 반영)
    LaunchedEffect(isTabReselected) {
        if (isTabReselected) {
            listState.animateScrollToItem(0)
            viewModel.refresh(childId)
        }
    }

    ParentAlarmScreen(
        state = state,
        onExpandClick = { alarmId -> viewModel.toggleExpand(alarmId) },
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
        // [임시 헤더] PR #31 ParentUserSection 머지 시 해당 컴포넌트로 교체 예정
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = KieroTheme.colors.black)
                .statusBarsPadding()
                .padding(top = 25.dp, bottom = 15.dp, start = 16.dp, end = 16.dp),
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

        if (state.alarms.isEmpty() && !state.isLoading) {
            // ✅ 데이터가 없을 때: 피그마 수치 반영 빈 화면
            EmptyAlarmView()
        } else {
            // ✅ 데이터가 있을 때: 알림 리스트 (PR #31의 18.dp 패딩 적용)
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 15.dp, bottom = 20.dp)
            ) {
                val sortedAlarms = state.alarms
                    .sortedWith(compareByDescending<ParentAlarmUiModel> { it.date }
                        .thenByDescending { it.time })

                val groupedAlarms = sortedAlarms.groupBy { it.date }

                groupedAlarms.forEach { (date, alarmsForDate) ->
                    item(key = "header_$date") {
                        ParentAlarmDateHeader(date = date)
                    }

                    items(
                        items = alarmsForDate,
                        key = { it.id }
                    ) { alarm ->
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

                // TODO: 무한 스크롤 로딩바 필요 시 추가 예정
            }
        }
    }
}

@Composable
private fun EmptyAlarmView() {
    // 전체를 가로로 꽉 채우는 컨테이너
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(KieroTheme.colors.black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. 피그마 top 253px 지점까지의 상단 여백
        Spacer(modifier = Modifier.height(175.dp))

        // 2. 텍스트와 이미지를 하나의 시각적 그룹으로 묶음
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "아직 아이로부터 도착한 알림이 없어요!",
                style = KieroTheme.typography.semiBold.title3,
                color = KieroTheme.colors.gray400,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 가로 비율을 꽉 채우는 이미지 (360:274)
            Image(
                painter = painterResource(id = R.drawable.img_parent_no_alarm),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(360f / 274f),
                contentScale = ContentScale.FillWidth // ✅ 좌우 여백 없이 꽉 채움
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, name = "빈 화면 (디자인 반영)")
@Composable
private fun EmptyPreview() {
    KieroTheme {
        ParentAlarmScreen(
            state = AlarmFeedState(alarms = persistentListOf()),
            onExpandClick = {},
            listState = rememberLazyListState(),
            paddingValues = PaddingValues(0.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, name = "알림 리스트 프리뷰")
@Composable
private fun ListPreview() {
    KieroTheme {
        val dummyAlarms = (1..3).map { index ->
            ParentAlarmUiModel(
                id = "id_$index",
                date = "2026.01.14.(수)",
                time = "1${index}:00",
                message = "근영이가 미션을 완료했어요.",
                highlightText = "근영",
                highlightColor = Color(0xFF00FFE1),
                coinUsed = 10,
                imageUrl = null,
                isExpanded = false
            )
        }.toPersistentList()

        ParentAlarmScreen(
            state = AlarmFeedState(alarms = dummyAlarms),
            onExpandClick = {},
            listState = rememberLazyListState(),
            paddingValues = PaddingValues(0.dp)
        )
    }
}