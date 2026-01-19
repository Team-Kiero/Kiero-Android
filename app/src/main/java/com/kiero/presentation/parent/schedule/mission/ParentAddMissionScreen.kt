package com.kiero.presentation.parent.schedule.mission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.schedule.mission.component.datepicker.component.CalendarBottomSheet
import com.kiero.presentation.parent.schedule.mission.component.missionadd.MissionAwardInfo
import com.kiero.presentation.parent.schedule.mission.component.missionadd.MissionAwardSelect
import com.kiero.presentation.parent.schedule.mission.component.missionmain.MissionCalendar
import com.kiero.presentation.parent.schedule.mission.state.ParentAddMissionSideEffect
import com.kiero.presentation.parent.schedule.mission.viewmodel.ParentAddMissionViewModel
import com.kiero.presentation.parent.schedule.plan.component.select.ScheduleTextField

@Composable
fun ParentAddMissionRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentAddMissionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val showBottomSheet by viewModel.showBottomSheet.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    val globalTrigger = LocalGlobalUiEventTrigger.current

    // TODO : Test 용 고정값, 받아온 childId를 사용해야 됨.

    LaunchedEffect(Unit) {
        viewModel.setChildId(1L)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ParentAddMissionSideEffect.ShowSnackbar -> globalTrigger.showSnackbar(
                    SnackbarState(message = effect.message)
                )

                is ParentAddMissionSideEffect.NavigateToMissionList -> {
                    navigateUp()
                }
            }
        }
    }

    when (val uiState = state) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = KieroTheme.colors.main)
            }
        }

        is UiState.Failure -> {
        }

        else -> {
            ParentAddMissionScreen(
                paddingValues = paddingValues,
                navigateUp = navigateUp,
                viewModel = viewModel,
                showBottomSheet = showBottomSheet,
                selectedDate = selectedDate,
            )
        }
    }

    ParentAddMissionScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        viewModel = viewModel,
        showBottomSheet = showBottomSheet,
        selectedDate = selectedDate,
    )
}

@Composable
fun ParentAddMissionScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentAddMissionViewModel,
    showBottomSheet: Boolean,
    selectedDate: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        KieroTopbar(
            title = "미션추가",
            leftIconRes = R.drawable.ic_close_light,
            rightIconRes = R.drawable.ic_check,
            leftIconClick = navigateUp,
            rightIconClick = viewModel::createMission,
        )

        ScheduleTextField(
            state = viewModel.missionNameState,
            placeholder = "미션 이름을 입력해주세요.",
            maxLength = 15
        )

        MissionCalendar(
            onDateClick = viewModel::onDateClick,
            dateText = selectedDate ?: "마감일을 선택해주세요"
        )

        MissionAwardInfo()

        Spacer(modifier = Modifier.height(12.dp))

        MissionAwardSelect(
            textFieldState = viewModel.awardTextFieldState,
            onAwardClick = viewModel::onAwardClick,
        )

        if (showBottomSheet) {
            CalendarBottomSheet(
                onDismissRequest = viewModel::onDismissBottomSheet,
                onDateSelected = viewModel::onDateSelected
            )
        }
    }
}

@Preview
@Composable
private fun ParentAddMissionScreenPreview() {
    KieroTheme {
        ParentAddMissionRoute(
            paddingValues = PaddingValues(),
            navigateUp = {}
        )
    }
}