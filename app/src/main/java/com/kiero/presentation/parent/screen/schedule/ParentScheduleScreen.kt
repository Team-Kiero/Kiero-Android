package com.kiero.presentation.parent.screen.schedule


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.common.extension.statusBarColor
import com.kiero.core.common.util.ParentFormatters
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.component.indicator.KieroLoadingIndicator
import com.kiero.core.designsystem.theme.Gray900
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.UiState
import com.kiero.core.model.trigger.RefreshState
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.core.trigger.LocalRefreshState
import com.kiero.data.parent.plan.model.NormalScheduleModel
import com.kiero.data.parent.plan.model.RecurringScheduleModel
import com.kiero.data.parent.plan.model.ScheduleModel
import com.kiero.data.parent.plan.model.toScheduleEditArgs
import com.kiero.data.parent.plan.model.toSelectedDate
import com.kiero.presentation.parent.component.ParentContentBottomSheet
import com.kiero.presentation.parent.component.PlanTabFab
import com.kiero.presentation.parent.screen.schedule.plan.ParentPlanScreen
import com.kiero.presentation.parent.screen.schedule.plan.navigation.ScheduleEdit
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleSideEffect
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleState
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentScheduleState.Companion.formatRepeatText
import com.kiero.presentation.parent.screen.schedule.viewmodel.ParentScheduleViewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ParentScheduleRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToScheduleAdd: (String, Boolean) -> Unit,
    navigateToScheduleEdit: (ScheduleEdit) -> Unit,
    navigateToAlarm: () -> Unit,
    viewModel: ParentScheduleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current

    LaunchedEffect(Unit) {
        viewModel.ensureChildIdAndStartSse()
    }

    viewModel.sideEffect.collectSideEffect { effect ->
        when (effect) {
            is ParentScheduleSideEffect.ShowSnackBar ->
                globalTrigger.showSnackbar(SnackbarState(effect.message))
            ParentScheduleSideEffect.ShowDialog -> globalTrigger.dialogTrigger.show {}
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
    ) {
        when (val state = uiState) {
            is UiState.Loading -> KieroLoadingIndicator()

            is UiState.Success -> ParentScheduleScreen(
                paddingValues = paddingValues,
                scheduleState = state.data,
                onDateChange = viewModel::onDateChange,
                onResetToToday = viewModel::resetToday,
                onDeleteConfirm = viewModel::deleteSchedule,
                onEditClick = navigateToScheduleEdit,
                navigateToScheduleAdd = {
                    navigateToScheduleAdd(state.data.navInitialDate, state.data.isFireLit)
                },
                navigateToAlarm = navigateToAlarm,
            )

            is UiState.Failure -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "에러: ${state.message}", color = KieroTheme.colors.white)
            }

            is UiState.Empty -> ParentScheduleScreen(
                paddingValues = paddingValues,
                scheduleState = ParentScheduleState(),
                onResetToToday = viewModel::resetToday,
                onDateChange = viewModel::onDateChange,
                onDeleteConfirm = viewModel::deleteSchedule,
                onEditClick = navigateToScheduleEdit,
                navigateToScheduleAdd = {
                    navigateToScheduleAdd(LocalDate.now().toString(), false)
                },
                navigateToAlarm = navigateToAlarm,
            )
        }
    }
}

@Composable
private fun ParentScheduleScreen(
    paddingValues: PaddingValues,
    scheduleState: ParentScheduleState,
    modifier: Modifier = Modifier,
    onResetToToday: () -> Unit,
    onDateChange: (Boolean) -> Unit,
    onDeleteConfirm: (scheduleId: Long, selectedDate: String, isIncludeFollowing: Boolean?) -> Unit,
    onEditClick: (ScheduleEdit) -> Unit,
    navigateToScheduleAdd: () -> Unit,
    navigateToAlarm: () -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedEventId by remember { mutableStateOf<String?>(null) }
    var isDeleteIncludeFollowing by remember { mutableStateOf(false) }
    var snapshotSchedule by remember { mutableStateOf<ScheduleModel?>(null) }

    val selectedSchedule: ScheduleModel? = selectedEventId?.toLongOrNull()?.let { id ->
        val model = scheduleState.planAllModel
        model?.normalSchedules?.find { it.scheduleId == id }
            ?: model?.recurringSchedules?.find { it.scheduleId == id }
    }

    /**
     * startTime > LocalTime.now() → 수정/삭제 가능
     * startTime <= LocalTime.now() → 수정/삭제 불가 (이미 시작된 일정)
     * startTime 파싱 실패 또는 null → 기본값 true로 가능
     * */
    val canShowEditDelete = remember(selectedSchedule) {
        selectedSchedule?.let { schedule ->
            val startTimeStr = when (schedule) {
                is NormalScheduleModel -> schedule.startTime
                is RecurringScheduleModel -> schedule.startTime
                else -> null
            }

            // 반복 일정은 날짜도 함께 고려
            val referenceDate = when (schedule) {
                is NormalScheduleModel -> runCatching { LocalDate.parse(schedule.date) }.getOrNull()
                is RecurringScheduleModel -> runCatching { LocalDate.parse(schedule.repeatStartDate) }.getOrNull()
                else -> null
            }

            startTimeStr?.let { timeStr ->
                runCatching {
                    val startTime = LocalTime.parse(timeStr.take(5), DateTimeFormatter.ofPattern("HH:mm"))
                    val today = LocalDate.now()

                    when {
                        referenceDate == null -> startTime.isAfter(LocalTime.now())
                        referenceDate.isAfter(today) -> true  // 미래 날짜면 무조건 수정/삭제 가능
                        referenceDate.isBefore(today) -> false // 과거 날짜면 불가
                        else -> startTime.isAfter(LocalTime.now()) // 오늘이면 시간 비교
                    }
                }.getOrDefault(true)
            } ?: true
        } ?: true
    }

    LaunchedEffect(canShowEditDelete) {
        Timber.e("canShowEditDelete: $canShowEditDelete")
    }

    LaunchedEffect(showDeleteDialog) {
        if (showDeleteDialog) isDeleteIncludeFollowing = false
    }

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
            ParentPlanScreen(
                state = scheduleState,
                onDateChange = onDateChange,
                onResetToday = onResetToToday,
                onContentClick = { id ->
                    selectedEventId = id
                    showBottomSheet = true
                }
            )
        }

        if (showBottomSheet) {
            ParentContentBottomSheet(
                topTitle = selectedSchedule?.name ?: "일정 상세",
                onDismissRequest = {
                    showBottomSheet = false
                    selectedEventId = null
                },
                onEditClick = if (canShowEditDelete) {
                    {
                        val captured = selectedSchedule
                        showBottomSheet = false
                        captured?.let { onEditClick(it.toScheduleEditArgs()) }
                        selectedEventId = null
                    }
                } else null,
                onDeleteClick = if (canShowEditDelete) {
                    {
                        snapshotSchedule = selectedSchedule
                        showBottomSheet = false
                        showDeleteDialog = true
                    }
                } else null,
                content = {
                    selectedSchedule?.let { ScheduleDetailContent(schedule = it) }
                }
            )
        }

        if (showDeleteDialog) {
            KieroDialog(
                onDismiss = { showDeleteDialog = false },
                title = snapshotSchedule?.name ?: "일정 삭제",
                subDescription = "삭제하시겠습니까?",
                cancelAction = KieroCancelAction(
                    text = "취소",
                    onClick = { showDeleteDialog = false }
                ),
                confirmAction = KieroConfirmAction(
                    text = "확인",
                    onClick = {
                        val id = snapshotSchedule?.scheduleId
                        val date = snapshotSchedule.toSelectedDate()
                        val includeFollowing = if (snapshotSchedule is RecurringScheduleModel) {
                            isDeleteIncludeFollowing
                        } else null

                        showDeleteDialog = false
                        selectedEventId = null
                        snapshotSchedule = null

                        if (id != null) {
                            onDeleteConfirm(id, date, includeFollowing)
                        }
                    }
                ),
                content = {
                    ScheduleDeleteDialogContent(
                        isIncludeFollowing = isDeleteIncludeFollowing,
                        onContentClick = { isDeleteIncludeFollowing = !isDeleteIncludeFollowing }
                    )
                }
            )
        }

        PlanTabFab(
            onScheduleAdd = { navigateToScheduleAdd() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
private fun ScheduleDeleteDialogContent(
    isIncludeFollowing: Boolean,
    onContentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconRes = if (isIncludeFollowing) {
        R.drawable.ic_parent_addschedule_check_on
    } else {
        R.drawable.ic_parent_addschedule_check_off
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onContentClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = "이후 반복되는 일정 포함",
            color = KieroTheme.colors.gray400,
            style = KieroTheme.typography.regular.body4
        )
    }
}

@Composable
private fun ScheduleDetailContent(
    schedule: ScheduleModel,
    modifier: Modifier = Modifier,
    contentsColor: Color = KieroTheme.colors.gray400,
    contentsStyle: TextStyle = KieroTheme.typography.regular.body4,
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        when (schedule) {
            is NormalScheduleModel -> {
                Text(
                    text = ParentFormatters.formatDateWithDayOfWeek(schedule.date),
                    style = contentsStyle,
                    color = contentsColor
                )
                Text(
                    text = "${schedule.startTime.take(5)}-${schedule.endTime.take(5)}",
                    style = contentsStyle,
                    color = contentsColor
                )
            }
            is RecurringScheduleModel -> {
                Text(
                    text = ParentFormatters.formatDateWithDayOfWeek(schedule.repeatStartDate),
                    style = contentsStyle,
                    color = contentsColor
                )
                Text(
                    text = "${schedule.startTime.take(5)}-${schedule.endTime.take(5)}",
                    style = contentsStyle,
                    color = contentsColor
                )
                Text(
                    text = formatRepeatText(schedule.dayOfWeek),
                    style = contentsStyle,
                    color = contentsColor
                )
            }
        }
    }
}

@Composable
@Preview
private fun ParentScheduleScreenPreview() {
    KieroTheme {
        CompositionLocalProvider(LocalRefreshState provides RefreshState()) {
            ParentScheduleScreen(
                paddingValues = PaddingValues(),
                scheduleState = ParentScheduleState(),
                onDateChange = {},
                onResetToToday = {},
                onDeleteConfirm = { _, _, _ -> },
                onEditClick = {},
                navigateToScheduleAdd = {},
                navigateToAlarm = {}
            )
        }
    }
}