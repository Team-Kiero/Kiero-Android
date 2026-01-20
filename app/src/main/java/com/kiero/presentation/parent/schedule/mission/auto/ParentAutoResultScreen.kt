package com.kiero.presentation.parent.schedule.mission.auto

import androidx.compose.foundation.background
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoMissionEditForm
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentLocalKieroSnackbar
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentMissionNavigator
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionState
import com.kiero.presentation.parent.schedule.mission.auto.viewmodel.AutoMissionViewModel
import com.kiero.presentation.parent.schedule.mission.component.datepicker.component.CalendarBottomSheet
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun ParentAutoResultRoute(
    paddingValues: PaddingValues,
    state: AutoMissionState,
    childId: Long,
    viewModel: AutoMissionViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.missions) {
        if (state.missions.isNotEmpty() && state.currentIndex == 0) {
            viewModel.updateCurrentIndex(0)
        }
    }
    ParentAutoResultScreen(
        state = state,
        onMissionNameChange = viewModel::updateMissionName,
        onDateSelected = viewModel::onDateSelected,
        onRewardClick = viewModel::onAwardClick,
        onIndexChange = viewModel::updateCurrentIndex,
        onSaveClick = { viewModel.saveAllMissions(childId) },
        onCancelClick = viewModel::handleCancel,
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
            .padding(paddingValues)
            .background(KieroTheme.colors.black),
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

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                state.missions.getOrNull(page)?.let { mission ->
                    ParentAutoMissionEditForm(
                        mission = mission,
                        selectedDate = state.selectedDate,
                        onMissionNameChange = onMissionNameChange,
                        onDateClick = onShowDatePicker,
                        onRewardClick = onRewardClick,
                        awardTextFieldState = awardTextFieldState
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 31.dp)
            ) {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    ParentLocalKieroSnackbar(
                        message = data.visuals.message,
                        modifier = Modifier.fillMaxWidth()
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
                onDateSelected = { dateString ->
                    try {
                        val date = LocalDate.parse(
                            dateString,
                            DateTimeFormatter.ISO_LOCAL_DATE
                        )
                        onDateSelected(date)
                    } catch (e: Exception) {
                    }
                }
            )
        }
    }
}