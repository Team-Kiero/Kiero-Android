package com.kiero.presentation.parent.schedule.mission.auto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentMissionEditForm
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentMissionNavigator
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionContract
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionEvent
import java.time.format.DateTimeFormatter

// Todo : 네비게이션 연결
@Composable
fun ParentAutoResultRoute(
    paddingValues: PaddingValues,
    state: AutoMissionContract,
    onEvent: (AutoMissionEvent) -> Unit,
    navigateUp: () -> Unit,
) {
    ParentAutoResultScreen(
        paddingValues = paddingValues,
        state = state,
        onEvent = onEvent,
        navigateUp = navigateUp
    )
}

@Composable
fun ParentAutoResultScreen(
    paddingValues: PaddingValues,
    state: AutoMissionContract,
    onEvent: (AutoMissionEvent) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(44.dp))

        // TopBar
        KieroTopbar(
            title = "알림장 미션 추가",
            leftIconRes = R.drawable.ic_close_light,
            rightIconRes = if (state.isSaveEnabled) R.drawable.ic_check else null,
            leftIconClick = { onEvent(AutoMissionEvent.OnCancelClick) },
            rightIconClick = if (state.isSaveEnabled) {
                { onEvent(AutoMissionEvent.OnSaveAllClick) }
            } else null
        )

        Spacer(modifier = Modifier.height(16.dp))


        // 미션 네비게이터
        ParentMissionNavigator(
            currentIndex = state.currentIndex,
            totalCount = state.missions.size,
            onPreviousClick = {
                onEvent(AutoMissionEvent.OnPageChanged(state.currentIndex - 1))
            },
            onNextClick = {
                onEvent(AutoMissionEvent.OnPageChanged(state.currentIndex + 1))
            },
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 미션 수정 폼
        state.currentMission?.let { mission ->
            ParentMissionEditForm(
                missionName = mission.name,
                dueDate = mission.dueAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.(E)")),
                reward = mission.reward,
                onNameChange = { onEvent(AutoMissionEvent.UpdateMissionName(it)) },
                onDateClick = { /* TODO: PR #35 머지 후 CalendarBottomSheet 연동 */ },
                onRewardChange = { onEvent(AutoMissionEvent.UpdateMissionReward(it)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 26.dp)
            )
        }

        Spacer(modifier = Modifier.height(55.dp))

        // 저장하기 버튼
        KieroButtonMedium(
            text = "저장하기",
            onClick = { onEvent(AutoMissionEvent.OnSaveAllClick) },
            isEnabled = state.isSaveEnabled,
            containerColor = KieroTheme.colors.main,
            contentColor = KieroTheme.colors.black,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Preview
@Composable
private fun ParentAutoResultScreenPreview() {
    KieroTheme {
        ParentAutoResultScreen(
            paddingValues = PaddingValues(),
            state = AutoMissionContract.FAKE,
            onEvent = {},
            navigateUp = {}
        )
    }
}