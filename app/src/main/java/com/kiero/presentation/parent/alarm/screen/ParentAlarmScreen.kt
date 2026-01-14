package com.kiero.presentation.parent.alarm.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.alarm.component.ParentAlarmCard
import com.kiero.presentation.parent.alarm.component.ParentAlarmDateHeader
import com.kiero.presentation.parent.alarm.state.AlarmFeedState
import com.kiero.presentation.parent.alarm.viewmodel.AlarmFeedViewModel
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ParentAlarmRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: AlarmFeedViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ParentAlarmScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        state = state,
        onExpandClick = { alarmId -> viewModel.toggleExpand(alarmId) }
    )
}

@Composable
private fun ParentAlarmScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    state: AlarmFeedState,
    onExpandClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        // TODO: 헤더 컴포넌트로 분리 (ParentAlarmHeader)
        //  - 왼쪽: 타이틀 없음
        //  - 오른쪽: 사용자 이름 + 프로필 아이콘
        //  - 클릭 시 로그아웃 다이얼로그
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                // 모든 수치를 패딩으로 제어 (상단 25, 하단 15, 좌우 15)
                .padding(top = 25.dp, bottom = 15.dp, start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "근영맘",  // TODO: 실제 사용자 이름으로 교체
                style = KieroTheme.typography.regular.body2,
            )
            Spacer(modifier = Modifier.width(8.dp))
            // TODO: 실제 프로필 이미지 아이콘으로 교체
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_profile),
                contentDescription = "프로필",
                modifier = Modifier.size(32.dp),
                tint = KieroTheme.colors.gray500
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            // 1. 좌우 16dp 유지, 상하 여백은 0으로 잡는 것이 헤더와의 간격(15dp)을 유지하기에 더 정확.
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            // 2. 아이템(헤더-카드, 카드-카드) 사이의 간격을 16dp로 변경
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val groupedAlarms = state.alarms.groupBy { it.date }

            groupedAlarms.forEach { (date, alarmsForDate) ->
                item(key = "header_$date") {
                    ParentAlarmDateHeader(date = date)
                }

                items(
                    items = alarmsForDate,
                    key = { it.id }
                ) { alarm ->
                    ParentAlarmCard(
                        modifier = Modifier.fillMaxWidth(),
                        time = alarm.time,
                        message = alarm.message,
                        highlightTexts = listOf(alarm.highlightText),
                        highlightColor = alarm.highlightColor,
                        coinUsed = alarm.coinUsed,
                        imageUrl = alarm.imageUrl,
                        isExpanded = alarm.isExpanded,
                        onExpandClick = { onExpandClick(alarm.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ParentAlarmScreenPreview() {
    KieroTheme {
        var state by remember { mutableStateOf(AlarmFeedState.FAKE) }

        ParentAlarmScreen(
            paddingValues = PaddingValues(0.dp),
            navigateUp = {},
            state = state,
            onExpandClick = { alarmId ->
                // Preview에서 상태 변경 처리
                state = state.copy(
                    alarms = state.alarms.map { alarm ->
                        if (alarm.id == alarmId) {
                            alarm.copy(isExpanded = !alarm.isExpanded)
                        } else {
                            alarm
                        }
                    }.toPersistentList()
                )
            }
        )
    }
}