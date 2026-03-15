package com.kiero.presentation.parent.screen.mission.overview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.common.extension.toRelativeDayFromDate
import com.kiero.core.common.util.ParentFormatters
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.model.viewtype.DisplayType
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.presentation.main.navigation.ParentMainTab
import com.kiero.presentation.parent.component.MissionTabFab
import com.kiero.presentation.parent.component.ParentContentBottomSheet
import com.kiero.presentation.parent.component.ParentTopbar
import com.kiero.presentation.parent.screen.mission.directadd.navigation.MissionEdit
import com.kiero.presentation.parent.screen.mission.overview.component.missionmain.MissionInfo
import com.kiero.presentation.parent.screen.mission.overview.component.missionmain.MissionListItem
import com.kiero.presentation.parent.screen.mission.overview.state.ParentMissionSideEffect
import com.kiero.presentation.parent.screen.mission.overview.state.ParentMissionState
import com.kiero.presentation.parent.screen.mission.overview.viewmodel.ParentMissionViewModel

@Composable
fun ParentMissionRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToMissionEdit: (MissionEdit) -> Unit,
    navigateToAddMission: () -> Unit,
    navigateToAutoMission: () -> Unit,
    viewModel: ParentMissionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current

    LaunchedEffect(Unit) { viewModel.getMissions() }

    viewModel.sideEffect.collectSideEffect { effect ->
        when (effect) {
            is ParentMissionSideEffect.ShowSnackbar ->
                globalTrigger.showSnackbar(SnackbarState(effect.message))

            ParentMissionSideEffect.RefreshMissions ->
                viewModel.getMissions()
        }
    }

    when (val uiState = state) {
        is UiState.Success -> ParentMissionScreen(
            paddingValues = paddingValues,
            state = uiState.data,
            isEmpty = false,
            navigateToMissionEdit = navigateToMissionEdit,
            onDeleteConfirm = viewModel::deleteMission,
            navigateToAddMission = navigateToAddMission,
            navigateToAutoMission = navigateToAutoMission,
        )

        is UiState.Empty -> ParentMissionScreen(
            paddingValues = paddingValues,
            state = ParentMissionState(),
            isEmpty = true,
            navigateToMissionEdit = navigateToMissionEdit,
            onDeleteConfirm = viewModel::deleteMission,
            navigateToAddMission = navigateToAddMission,
            navigateToAutoMission = navigateToAutoMission,
        )

        is UiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) { CircularProgressIndicator(color = KieroTheme.colors.main) }

        else -> Unit
    }
}

@Composable
private fun ParentMissionScreen(
    paddingValues: PaddingValues,
    state: ParentMissionState,
    isEmpty: Boolean,
    navigateToMissionEdit: (MissionEdit) -> Unit,
    onDeleteConfirm: (missionId: Long) -> Unit,
    navigateToAddMission: () -> Unit,
    navigateToAutoMission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = LocalRefreshState.current
    val listState = rememberLazyListState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isFabExpanded by remember { mutableStateOf(false) }

    var selectedId by remember { mutableLongStateOf(-1L) }
    var selectedName by remember { mutableStateOf("") }
    var selectedReward by remember { mutableStateOf(0) }
    var selectedDueAt by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        refreshState.refreshEvent.collect { tab ->
            if (tab == ParentMainTab.MISSION) listState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ParentTopbar(title = "미션", onAlarmClick = { })

            if (isEmpty) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = KieroTheme.colors.black),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_mission_empty_view),
                        contentDescription = null,
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = KieroTheme.colors.black),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    state.kidMissionByDateList.missionsByDate.forEach { missionsByDate ->
                        stickyHeader {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = KieroTheme.colors.black)
                                    .padding(vertical = 8.dp),
                            ) {
                                MissionInfo(
                                    dayOfWeek = missionsByDate.dueAt.toRelativeDayFromDate,
                                    dueAt = ParentFormatters.formatDateWithDayOfWeek(missionsByDate.dueAt),
                                )
                            }
                        }
                        items(items = missionsByDate.missions) { mission ->
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                                    .noRippleClickable {
                                        selectedId = mission.id
                                        selectedName = mission.name
                                        selectedReward = mission.reward
                                        selectedDueAt = missionsByDate.dueAt
                                        showBottomSheet = true
                                    }
                            ) {
                                MissionListItem(
                                    missionTitle = mission.name,
                                    reward = mission.reward
                                )
                            }
                        }
                    }
                }
            }
        }

        MissionTabFab(
            isExpanded = isFabExpanded,
            onExpandedChange = { isFabExpanded = it },
            onMissionAdd = navigateToAddMission,
            onMissionRecommend = navigateToAutoMission,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        )

        if (showBottomSheet) {
            ParentContentBottomSheet(
                topTitle = selectedName,
                onDismissRequest = { showBottomSheet = false },
                onEditClick = {
                    showBottomSheet = false
                    navigateToMissionEdit(
                        MissionEdit(
                            missionId = selectedId,
                            name = selectedName,
                            reward = selectedReward,
                            dueAt = selectedDueAt,
                        )
                    )
                },
                onDeleteClick = {
                    showBottomSheet = false
                    showDeleteDialog = true
                },
                content = {
                    MissionBottomSheetContent(
                        reward = selectedReward,
                        dueAt = selectedDueAt,
                    )
                },
            )
        }

        if (showDeleteDialog) {
            KieroDialog(
                onDismiss = { showDeleteDialog = false },
                title = selectedName,
                subDescription = "미션을 삭제하시겠습니까?",
                cancelAction = KieroCancelAction(
                    text = "취소",
                    onClick = { showDeleteDialog = false }),
                confirmAction = KieroConfirmAction(
                    text = "확인",
                    onClick = {
                        showDeleteDialog = false
                        if (selectedId != -1L) onDeleteConfirm(selectedId)
                    },
                ),
            )
        }
    }
}

@Composable
private fun MissionBottomSheetContent(
    reward: Int,
    dueAt: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Text(
            text = ParentFormatters.formatDateWithDayOfWeek(dueAt),
            color = KieroTheme.colors.gray200,
            style = KieroTheme.typography.semiBold.title3
        )

        KieroChip(
            isEnabled = false,
            isCompleted = true,
            action = KieroCoinAction(
                coinCount = reward,
                isCompleted = true,
                isEnabled = false,
                viewType = DisplayType.KID,
                onClick = {}
            )
        )
    }
}


@Preview
@Composable
private fun ParentMissionScreenPreview() {
    KieroTheme {
        ParentMissionRoute(
            paddingValues = PaddingValues(),
            navigateUp = {},
            navigateToMissionEdit = {},
            navigateToAddMission = {},
            navigateToAutoMission = {},
        )
    }
}