package com.kiero.presentation.parent.screen.mission.autoadd

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.mission.autoadd.component.ParentAutoMissionEditForm
import com.kiero.presentation.parent.screen.mission.autoadd.component.ParentMissionNavigator
import com.kiero.presentation.parent.screen.mission.autoadd.model.MissionUiModel
import com.kiero.presentation.parent.screen.mission.autoadd.state.AutoMissionSideEffect
import com.kiero.presentation.parent.screen.mission.autoadd.state.AutoMissionState
import com.kiero.presentation.parent.screen.mission.autoadd.viewmodel.AutoMissionViewModel
import com.kiero.presentation.parent.screen.mission.overview.component.datepicker.component.CalendarBottomSheet
import java.time.LocalDate


@Composable
fun ParentAutoResultRoute(
    paddingValues: PaddingValues,
    state: AutoMissionState,
    navigateUp: () -> Unit,
    viewModel: AutoMissionViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState(
        initialPage = state.currentIndex,
        pageCount = { state.missions.size }
    )
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    viewModel.sideEffect.collectSideEffect { effect ->
        when (effect) {
            is AutoMissionSideEffect.ScrollToPage -> {
                pagerState.animateScrollToPage(effect.index)
            }
            else -> {}
        }
    }

    LaunchedEffect(state.missions) {
        if (state.missions.isNotEmpty() && state.currentIndex == 0) {
            viewModel.updateCurrentIndex(0)
        }
    }

    ParentAutoResultScreen(
        pagerState = pagerState,
        currentIndex = state.currentIndex,
        missions = state.missions,
        selectedDate = state.selectedDate,
        isSaveEnabled = state.isSaveEnabled,
        isSaving = state.isSaving,
        showBottomSheet = showBottomSheet,
        onMissionNameChange = viewModel::updateMissionName,
        onDateSelected = viewModel::updateMissionDate,
        onRewardClick = viewModel::onAwardClick,
        onIndexChange = viewModel::updateCurrentIndex,
        onSaveClick = { viewModel.saveAllMissions() },
        onCancelClick = viewModel::backToInputScreen,
        onShowDatePicker = { showBottomSheet = true },
        onDismissDatePicker = { showBottomSheet = false },
        awardTextFieldState = viewModel.awardTextFieldState,
        paddingValues = paddingValues,
    )
}

@Composable
fun ParentAutoResultScreen(
    pagerState: PagerState,
    currentIndex: Int,
    missions: List<MissionUiModel>,
    selectedDate: LocalDate?,
    isSaveEnabled: Boolean,
    isSaving: Boolean,
    showBottomSheet: Boolean,
    onMissionNameChange: (String) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onRewardClick: (Int) -> Unit,
    onIndexChange: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onShowDatePicker: () -> Unit,
    onDismissDatePicker: () -> Unit,
    awardTextFieldState: TextFieldState,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {

    val focusManager = LocalFocusManager.current

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentIndex) {
            onIndexChange(pagerState.currentPage)
        }
    }

    LaunchedEffect(currentIndex) {
        if (pagerState.currentPage != currentIndex) {
            pagerState.animateScrollToPage(currentIndex)
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
            currentIndex = currentIndex,
            totalCount = missions.size,
            onPreviousClick = { onIndexChange(currentIndex - 1) },
            onNextClick = { onIndexChange(currentIndex + 1) },
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            missions.getOrNull(page)?.let { mission ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    ParentAutoMissionEditForm(
                        mission = mission,
                        selectedDate = selectedDate,
                        onMissionNameChange = onMissionNameChange,
                        onDateClick = onShowDatePicker,
                        onRewardClick = onRewardClick,
                        awardTextFieldState = awardTextFieldState,
                    )
                }
            }
        }

        Spacer(Modifier.height(55.dp))

        KieroButtonMedium(
            text = "저장하기",
            onClick = onSaveClick,
            isEnabled = isSaveEnabled && !isSaving,
            containerColor = KieroTheme.colors.main,
            contentColor = KieroTheme.colors.black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(28.dp))

        if (showBottomSheet) {
            CalendarBottomSheet(
                onDismissRequest = onDismissDatePicker,
                onDateSelected = onDateSelected
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ParentAutoResultScreenPreview() {
    val awardTextFieldState = rememberTextFieldState("20")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    KieroTheme {
        ParentAutoResultScreen(
            pagerState = pagerState,
            currentIndex = 0,
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
            selectedDate = LocalDate.now().plusDays(1),
            isSaveEnabled = true,
            isSaving = false,
            showBottomSheet = false,
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
        )
    }
}