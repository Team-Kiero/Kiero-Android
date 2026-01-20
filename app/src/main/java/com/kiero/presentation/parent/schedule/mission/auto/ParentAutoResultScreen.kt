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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoInputField
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoMissionAwardInfo
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoMissionAwardSelect
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoMissionCalendar
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoMissionEditForm
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentLocalKieroSnackbar
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentMissionNavigator
import com.kiero.presentation.parent.schedule.mission.auto.viewmodel.AutoMissionViewModel
import com.kiero.presentation.parent.schedule.mission.component.datepicker.component.CalendarBottomSheet
import java.time.LocalDate

@Composable
fun ParentAutoResultRoute(
    paddingValues: PaddingValues,
    childId: Long,
    navigateUp: () -> Unit,
    viewModel: AutoMissionViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.shouldNavigateBack.collect {
            navigateUp()
        }
    }

    ParentAutoResultScreen(
        paddingValues = paddingValues,
        viewModel = viewModel,
        childId = childId,
        navigateUp = navigateUp
    )
}

@Composable
fun ParentAutoResultScreen(
    paddingValues: PaddingValues,
    viewModel: AutoMissionViewModel,
    childId: Long,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val missions by viewModel.missions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val pagerState = rememberPagerState(
        initialPage = currentIndex,
        pageCount = { missions.size }
    )

    val isSaveEnabled = missions.all {
        it.name.isNotBlank() && !it.dueAt.isBefore(LocalDate.now()) && it.reward > 0
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentIndex) {
            viewModel.updateCurrentIndex(pagerState.currentPage)
        }
    }

    LaunchedEffect(currentIndex) {
        if (pagerState.currentPage != currentIndex) {
            pagerState.animateScrollToPage(currentIndex)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
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
            rightIconRes = if (isSaveEnabled) R.drawable.ic_check else null,
            leftIconClick = { viewModel.handleCancel() },
            rightIconClick = if (isSaveEnabled) {
                { viewModel.saveAllMissions(childId) }
            } else {
                {}
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ParentMissionNavigator(
            currentIndex = currentIndex,
            totalCount = missions.size,
            onPreviousClick = { viewModel.updateCurrentIndex(currentIndex - 1) },
            onNextClick = { viewModel.updateCurrentIndex(currentIndex + 1) },
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
                missions.getOrNull(page)?.let { mission ->
                    ParentAutoMissionEditForm(
                        mission = mission,
                        selectedDate = selectedDate,
                        viewModel = viewModel
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
            onClick = { viewModel.saveAllMissions(childId) },
            isEnabled = isSaveEnabled && !isSaving,
            containerColor = KieroTheme.colors.main,
            contentColor = KieroTheme.colors.black,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(28.dp))

        if (showBottomSheet) {
            CalendarBottomSheet(
                onDismissRequest = { viewModel.onDismissBottomSheet() },
                onDateSelected = { viewModel.onDateSelected(it) }
            )
        }
    }
}

@Preview
@Composable
private fun ParentAutoResultScreen_WithSnackbarPreview() {
    KieroTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(KieroTheme.colors.black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(44.dp))

            KieroTopbar(
                title = "알림장 미션 추가",
                leftIconRes = R.drawable.ic_close_light,
                rightIconRes = R.drawable.ic_check,
                leftIconClick = {},
                rightIconClick = {}
            )

            Spacer(Modifier.height(16.dp))

            ParentMissionNavigator(
                currentIndex = 0,
                totalCount = 3,
                onPreviousClick = {},
                onNextClick = {}
            )

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Preview용 간단한 미션 폼
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = KieroTheme.colors.gray900,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(12.dp),
                ) {
                    ParentAutoInputField(
                        text = "수학 숙제하기",
                        onTextChange = {},
                        placeholder = "미션 이름을 입력해주세요.",
                        maxLength = 15,
                        singleLine = true
                    )

                    ParentAutoMissionCalendar(
                        dateText = "2024.01.20.(월)",
                        onDateClick = {}
                    )

                    ParentAutoMissionAwardInfo()

                    Spacer(Modifier.height(12.dp))

                    ParentAutoMissionAwardSelect(
                        textFieldState = TextFieldState(),
                        onAwardClick = {}
                    )
                }
            }

            Spacer(Modifier.height(55.dp))

            KieroButtonMedium(
                text = "저장하기",
                onClick = {},
                isEnabled = true,
                containerColor = KieroTheme.colors.main,
                contentColor = KieroTheme.colors.black,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(28.dp))
        }
    }
}