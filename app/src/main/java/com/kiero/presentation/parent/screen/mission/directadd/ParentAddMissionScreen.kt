package com.kiero.presentation.parent.screen.mission.directadd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.screen.mission.component.datepicker.component.CalendarBottomSheet
import com.kiero.presentation.parent.screen.mission.directadd.component.missionadd.MissionAwardInfo
import com.kiero.presentation.parent.screen.mission.directadd.component.missionadd.MissionAwardSelect
import com.kiero.presentation.parent.screen.mission.component.missionmain.MissionCalendar
import com.kiero.presentation.parent.screen.mission.directadd.state.ParentAddMissionSideEffect
import com.kiero.presentation.parent.screen.mission.directadd.state.ParentAddMissionState
import com.kiero.presentation.parent.screen.mission.directadd.viewmodel.ParentAddMissionViewModel
import com.kiero.presentation.parent.screen.schedule.plan.component.select.ScheduleTextField

@Composable
fun ParentAddMissionRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentAddMissionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val showBottomSheet by viewModel.showBottomSheet.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    val dateText = viewModel.displayDate
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.setChildId()
    }

    viewModel.sideEffect.collectSideEffect { effect ->
        when (effect) {
            is ParentAddMissionSideEffect.ShowSnackbar -> {
                focusManager.clearFocus()

                globalTrigger.showSnackbar(
                    SnackbarState(message = effect.message),
                )
            }

            is ParentAddMissionSideEffect.NavigateToMissionList -> {
                navigateUp()
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
                selectedDate = dateText,
                state = state
            )
        }
    }

}

@Composable
fun ParentAddMissionScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentAddMissionViewModel,
    showBottomSheet: Boolean,
    selectedDate: String?,
    state: ParentAddMissionState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
            .noRippleClickable {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        KieroTopbar(
            title = "미션추가",
            leftIconRes = R.drawable.ic_close_light,
            rightIconRes = R.drawable.ic_check,
            leftIconClick = navigateUp,
            rightIconClick = {
                if (!state.isLoading) {
                    viewModel.createMission()
                }
            },
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