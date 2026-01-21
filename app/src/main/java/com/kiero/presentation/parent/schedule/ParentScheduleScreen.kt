package com.kiero.presentation.parent.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.statusBarColor
import com.kiero.core.common.util.successData
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.Gray900
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.presentation.parent.component.MissionTabFab
import com.kiero.presentation.parent.component.ParentTabRow
import com.kiero.presentation.parent.component.ParentUserSection
import com.kiero.presentation.parent.component.PlanTabFab
import com.kiero.presentation.parent.schedule.mission.ParentMissionRoute
import com.kiero.presentation.parent.schedule.model.TabItem
import com.kiero.presentation.parent.schedule.plan.ParentPlanScreen
import com.kiero.presentation.parent.schedule.plan.state.ParentScheduleState
import com.kiero.presentation.parent.schedule.viewmodel.ParentScheduleViewModel
import com.kiero.presentation.signup.parent.state.ParentSignUpSideEffect
import com.kiero.presentation.signup.parent.state.ParentSignUpState
import java.time.LocalDate

@Composable
fun ParentScheduleRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToSelection: () -> Unit,
    navigateToScheduleAdd: (String, Boolean) -> Unit,
    navigateToMissionAdd: () -> Unit,
    navigateToAutoMissionAdd: (Long) -> Unit,
    viewModel: ParentScheduleViewModel = hiltViewModel(),
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val authState by viewModel.authstate.collectAsStateWithLifecycle()
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchSchedule()
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is ParentSignUpSideEffect.NavigateToSelection -> navigateToSelection()

            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val state = uiState) {
            is UiState.Loading -> {
                KieroLoadingIndicator()
            }

            is UiState.Success -> {
                ParentScheduleScreen(
                    paddingValues = paddingValues,
                    state = authState,
                    scheduleState = state.data,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = viewModel::updateTabIndex,
                    onDateChange = viewModel::onDateChange,
                    onResetToToday = viewModel::resetToday,
                    navigateToScheduleAdd = {
                        navigateToScheduleAdd(
                            state.data.currentDate.toString(),
                            state.data.isFireLit
                        )
                    },
                    navigateToMissionAdd = navigateToMissionAdd,
                    navigateToAutoMissionAdd = navigateToAutoMissionAdd,
                    onUserNameClick = viewModel::onProfileClick
                )
            }

            is UiState.Failure -> {
                // 에러 시 스낵바나 재시도 버튼 표시
            }

            is UiState.Empty -> {
                ParentScheduleScreen(
                    paddingValues = paddingValues,
                    state = authState,
                    scheduleState = ParentScheduleState(),
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = viewModel::updateTabIndex,
                    onResetToToday = viewModel::resetToday,
                    onDateChange = viewModel::onDateChange,
                    onUserNameClick = viewModel::onProfileClick,
                    navigateToScheduleAdd = {
                        navigateToScheduleAdd(LocalDate.now().toString(), false)
                    },
                    navigateToMissionAdd = navigateToMissionAdd,
                    navigateToAutoMissionAdd = navigateToAutoMissionAdd,
                )
            }
        }

        if (uiState.successData?.isLogoutDialogVisible == true) {
            KieroDialog(
                title = "로그아웃",
                subDescription = "로그아웃 하시겠습니까?",
                onDismiss = viewModel::onLogoutCancel,
                confirmAction = KieroConfirmAction(
                    text = "확인",
                    onClick = {
                        viewModel.onLogoutConfirm()
                    }
                ),
                cancelAction = KieroCancelAction(
                    text = "취소",
                    onClick = viewModel::onLogoutCancel
                ),
                isDisabled = true,
                content = {}
            )
        }

        if (uiState.successData?.isLoading == true) {
            KieroLoadingIndicator()
        }
    }
}

@Composable
private fun ParentScheduleScreen(
    paddingValues: PaddingValues,
    state: ParentSignUpState,
    scheduleState: ParentScheduleState,
    navigateToAutoMissionAdd: (Long) -> Unit,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    onResetToToday: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onDateChange: (Boolean) -> Unit,
    onUserNameClick: () -> Unit,
    navigateToScheduleAdd: () -> Unit,
    navigateToMissionAdd: () -> Unit,
) {
    val tabs = remember { TabItem.entries.map { it.title } }
    var isMissionFabExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(color = KieroTheme.colors.black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarColor(backgroundColor = Gray900)
                .padding(paddingValues)
        ) {
            ParentUserSection(
                userName = state.parentInfo.parentName,
                profileImage = state.parentInfo.parentProfileImage,
                onUserNameClick = onUserNameClick,
                modifier = Modifier
                    .background(color = KieroTheme.colors.gray900)
            )

            ParentTabRow(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = onTabSelected
            )

            when (selectedTabIndex) {
                0 -> ParentPlanScreen(
                    state = scheduleState,
                    onDateChange = onDateChange,
                    onResetToday = onResetToToday,
                )

                1 -> ParentMissionRoute(paddingValues)
            }
        }

        if (selectedTabIndex == 1 && isMissionFabExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KieroTheme.colors.black.copy(alpha = 0.75f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isMissionFabExpanded = false
                    }
            )
        }

        when (selectedTabIndex) {
            0 -> PlanTabFab(
                onScheduleAdd = { navigateToScheduleAdd() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )

            1 -> MissionTabFab(
                isExpanded = isMissionFabExpanded,
                onExpandedChange = { isMissionFabExpanded = it },
                onMissionAdd = navigateToMissionAdd,
                onMissionRecommend = {
                    navigateToAutoMissionAdd(10) // 임시 ChildID
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 52.dp)
            )
        }
    }
}

@Composable
@Preview
private fun ParentScheduleScreenPreview() {
    KieroTheme {
        ParentScheduleRoute(
            paddingValues = PaddingValues(),
            navigateUp = {},
            navigateToScheduleAdd = { _, _ -> },
            navigateToMissionAdd = {},
            navigateToAutoMissionAdd = {},
            navigateToSelection = {}
        )
    }
}