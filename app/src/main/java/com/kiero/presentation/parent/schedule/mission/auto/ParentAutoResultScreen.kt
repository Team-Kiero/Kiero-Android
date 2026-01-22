package com.kiero.presentation.parent.schedule.mission.auto

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoMissionEditForm
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentMissionNavigator
import com.kiero.presentation.parent.schedule.mission.auto.model.MissionUiModel
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionSideEffect
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionState
import com.kiero.presentation.parent.schedule.mission.auto.viewmodel.AutoMissionViewModel
import com.kiero.presentation.parent.schedule.mission.component.datepicker.component.CalendarBottomSheet
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun ParentAutoResultRoute(
    paddingValues: PaddingValues,
    state: AutoMissionState,
    viewModel: AutoMissionViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.missions) {
        if (state.missions.isNotEmpty() && state.currentIndex == 0) {
            viewModel.updateCurrentIndex(0)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AutoMissionSideEffect.ShowToast -> {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
                else -> {}
            }
        }
    }
    ParentAutoResultScreen(
        state = state,
        onMissionNameChange = viewModel::updateMissionName,
        onDateSelected = viewModel::onDateSelected,
        onRewardClick = viewModel::onAwardClick,
        onIndexChange = viewModel::updateCurrentIndex,
        onSaveClick = { viewModel.saveAllMissions() },
        onCancelClick = viewModel::backToInputScreen,
        onShowDatePicker = viewModel::showDatePicker,
        onDismissDatePicker = viewModel::dismissDatePicker,
        awardTextFieldState = viewModel.awardTextFieldState,
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun ParentAutoResultScreen(
    state: AutoMissionState,
    onMissionNameChange: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onRewardClick: (Int) -> Unit,
    onIndexChange: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onShowDatePicker: () -> Unit,
    onDismissDatePicker: () -> Unit,
    awardTextFieldState: androidx.compose.foundation.text.input.TextFieldState,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {

    val focusManager = LocalFocusManager.current

    val pagerState = rememberPagerState(
        initialPage = state.currentIndex,
        pageCount = { state.missions.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != state.currentIndex) {
            onIndexChange(pagerState.currentPage)
        }
    }

    LaunchedEffect(state.currentIndex) {
        if (pagerState.currentPage != state.currentIndex) {
            pagerState.animateScrollToPage(state.currentIndex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(44.dp))

        KieroTopbar(
            title = "알림장 미션 추가",
            leftIconRes = R.drawable.ic_close_light,
            leftIconClick = onCancelClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        ParentMissionNavigator(
            currentIndex = state.currentIndex,
            totalCount = state.missions.size,
            onPreviousClick = { onIndexChange(state.currentIndex - 1) },
            onNextClick = { onIndexChange(state.currentIndex + 1) },
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            state.missions.getOrNull(page)?.let { mission ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    ParentAutoMissionEditForm(
                        mission = mission,
                        selectedDate = state.selectedDate,
                        onMissionNameChange = onMissionNameChange,
                        onDateClick = onShowDatePicker,
                        onRewardClick = onRewardClick,
                        awardTextFieldState = awardTextFieldState,
                        snackbarHostState = snackbarHostState  // 추가
                    )
                }
            }
        }

        Spacer(Modifier.height(55.dp))

        KieroButtonMedium(
            text = "저장하기",
            onClick = onSaveClick,
            isEnabled = state.isSaveEnabled && !state.isSaving,
            containerColor = KieroTheme.colors.main,
            contentColor = KieroTheme.colors.black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(28.dp))

        if (state.showBottomSheet) {
            CalendarBottomSheet(
                onDismissRequest = onDismissDatePicker,
                onDateSelected = onDateSelected
            )
        }
    }
}
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    device = "spec:width=360dp,height=800dp,dpi=420"
)
@Composable
private fun ParentAutoResultScreenPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val awardTextFieldState = rememberTextFieldState("20")

    LaunchedEffect(Unit) {
        snackbarHostState.showSnackbar("보상은 500개까지 설정할 수 있어요.")
    }

    KieroTheme {
        ParentAutoResultScreen(
            state = AutoMissionState(
                missions = listOf(
                    MissionUiModel(
                        name = "수학 숙제하기",
                        dueAt = LocalDate.now().plusDays(1),
                        reward = 20,
                        isCompleted = false
                    ),
                    MissionUiModel(
                        name = "체육복 챙기기",
                        dueAt = LocalDate.now().plusDays(2),
                        reward = 30,
                        isCompleted = false
                    )
                ),
                currentIndex = 0,
                selectedDate = LocalDate.now().plusDays(1)
            ),
            onMissionNameChange = {},
            onDateSelected = {},
            onRewardClick = {},
            onIndexChange = {},
            onSaveClick = {},
            onCancelClick = {},
            onShowDatePicker = {},
            onDismissDatePicker = {},
            awardTextFieldState = awardTextFieldState,
            paddingValues = PaddingValues(0.dp),
            snackbarHostState = snackbarHostState
        )
    }
}

@Preview(
    name = "Multiple Missions",
    showBackground = true,
    backgroundColor = 0xFF000000,
    device = "spec:width=360dp,height=800dp,dpi=420"
)
@Composable
private fun ParentAutoResultScreenMultipleMissionsPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val awardTextFieldState = rememberTextFieldState("20")

    KieroTheme {
        ParentAutoResultScreen(
            state = AutoMissionState(
                missions = listOf(
                    MissionUiModel(
                        name = "수학 숙제하기",
                        dueAt = LocalDate.now().plusDays(1),
                        reward = 20,
                        isCompleted = false
                    ),
                    MissionUiModel(
                        name = "체육복 챙기기",
                        dueAt = LocalDate.now().plusDays(2),
                        reward = 30,
                        isCompleted = false
                    ),
                    MissionUiModel(
                        name = "일기 쓰기",
                        dueAt = LocalDate.now().plusDays(1),
                        reward = 50,
                        isCompleted = false
                    )
                ),
                currentIndex = 1,
                selectedDate = LocalDate.now().plusDays(2)
            ),
            onMissionNameChange = {},
            onDateSelected = {},
            onRewardClick = {},
            onIndexChange = {},
            onSaveClick = {},
            onCancelClick = {},
            onShowDatePicker = {},
            onDismissDatePicker = {},
            awardTextFieldState = awardTextFieldState,
            paddingValues = PaddingValues(0.dp),
            snackbarHostState = snackbarHostState
        )
    }
}