package com.kiero.presentation.parent.screen.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
import java.time.LocalDate

enum class DeleteRepeatOption { THIS_WEEK_ONLY, INCLUDE_FOLLOWING }

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

            ParentScheduleSideEffect.ShowDialog ->
                globalTrigger.dialogTrigger.show {}

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
                isScheduleEditable = viewModel::isScheduleEditable
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
                isScheduleEditable = viewModel::isScheduleEditable
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
    onDeleteConfirm: (scheduleId: Long, selectedDate: String, isRecurring: Boolean, isIncludeFollowing: Boolean?) -> Unit,
    onEditClick: (ScheduleEdit) -> Unit,
    navigateToScheduleAdd: () -> Unit,
    navigateToAlarm: () -> Unit,
    isScheduleEditable: (ScheduleModel, String?) -> Boolean,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedEventId by remember { mutableStateOf<String?>(null) }
    var selectedEventDate by remember { mutableStateOf<String?>(null) }
    var deleteRepeatOption by remember { mutableStateOf(DeleteRepeatOption.THIS_WEEK_ONLY) }
    var snapshotSchedule by remember { mutableStateOf<ScheduleModel?>(null) }

    val selectedSchedule: ScheduleModel? = remember(
        selectedEventId,
        selectedEventDate,
        scheduleState.planAllModel
    ) {
        val id = selectedEventId?.toLongOrNull() ?: return@remember null
        val date = selectedEventDate
        val model = scheduleState.planAllModel

        model?.normalSchedules?.find { it.scheduleId == id && it.date.take(10) == date?.take(10) }
            ?: model?.recurringSchedules?.find { it.scheduleId == id }
    }

    val canShowEditDelete = remember(selectedSchedule, selectedEventDate) {
        selectedSchedule?.let { isScheduleEditable(it, selectedEventDate) } ?: false
    }

    LaunchedEffect(showDeleteDialog) {
        if (showDeleteDialog) deleteRepeatOption = DeleteRepeatOption.THIS_WEEK_ONLY
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
                onContentClick = { id, date ->
                    selectedEventId = id
                    selectedEventDate = date
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
                    selectedEventDate = null
                },
                onEditClick = if (canShowEditDelete) {
                    {
                        val captured = selectedSchedule
                        val clickedDate = selectedEventDate
                        showBottomSheet = false

                        captured?.let { schedule ->
                            val editArgs = schedule
                                .toScheduleEditArgs()
                                .copy(
                                    selectedDate = clickedDate ?: schedule.toSelectedDate()
                                )
                            onEditClick(editArgs)
                        }

                        selectedEventId = null
                        selectedEventDate = null
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
                    selectedSchedule?.let { schedule ->
                        ScheduleDetailContent(
                            schedule = schedule,
                            selectedDate = selectedEventDate
                        )
                    }
                }
            )
        }

        if (showDeleteDialog) {
            val isSnapshotRecurring = snapshotSchedule is RecurringScheduleModel

            KieroDialog(
                onDismiss = { showDeleteDialog = false },
                title = snapshotSchedule?.name ?: "일정 삭제",
                subDescription = if (isSnapshotRecurring) null else "삭제하시겠습니까?",
                cancelAction = KieroCancelAction(
                    text = "취소",
                    onClick = { showDeleteDialog = false }
                ),
                confirmAction = KieroConfirmAction(
                    text = "확인",
                    onClick = {
                        val id = snapshotSchedule?.scheduleId
                        val date = selectedEventDate ?: snapshotSchedule.toSelectedDate()
                        val includeFollowing = if (isSnapshotRecurring) {
                            deleteRepeatOption == DeleteRepeatOption.INCLUDE_FOLLOWING
                        } else {
                            null
                        }

                        showDeleteDialog = false
                        selectedEventId = null
                        selectedEventDate = null
                        snapshotSchedule = null

                        if (id != null) {
                            onDeleteConfirm(id, date, isSnapshotRecurring, includeFollowing)
                        }
                    }
                ),
                content = if (isSnapshotRecurring) {
                    {
                        ScheduleDeleteDialogContent(
                            selectedOption = deleteRepeatOption,
                            onOptionClick = { deleteRepeatOption = it }
                        )
                    }
                } else null
            )
        }

        PlanTabFab(
            onScheduleAdd = { navigateToScheduleAdd() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 27.dp, bottom = 24.dp + paddingValues.calculateBottomPadding())
        )
    }
}

@Composable
private fun ScheduleDeleteDialogContent(
    selectedOption: DeleteRepeatOption,
    onOptionClick: (DeleteRepeatOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .offset(y = 20.dp)
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "삭제하시겠습니까?",
            color = KieroTheme.colors.gray100,
            style = KieroTheme.typography.regular.body3,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.noRippleClickable {
                    onOptionClick(DeleteRepeatOption.THIS_WEEK_ONLY)
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val isSelected = selectedOption == DeleteRepeatOption.THIS_WEEK_ONLY
                val iconRes =
                    if (isSelected) R.drawable.ic_parent_addschedule_check_on
                    else R.drawable.ic_parent_addschedule_check_off
                val textColor =
                    if (isSelected) KieroTheme.colors.main
                    else KieroTheme.colors.gray400

                Icon(
                    imageVector = ImageVector.vectorResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "이번 주차만 포함",
                    color = textColor,
                    style = KieroTheme.typography.regular.body4
                )
            }

            Row(
                modifier = Modifier.noRippleClickable {
                    onOptionClick(DeleteRepeatOption.INCLUDE_FOLLOWING)
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val isSelected = selectedOption == DeleteRepeatOption.INCLUDE_FOLLOWING
                val iconRes =
                    if (isSelected) R.drawable.ic_parent_addschedule_check_on
                    else R.drawable.ic_parent_addschedule_check_off
                val textColor =
                    if (isSelected) KieroTheme.colors.main
                    else KieroTheme.colors.gray400

                Icon(
                    imageVector = ImageVector.vectorResource(id = iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "이후 일정 포함",
                    color = textColor,
                    style = KieroTheme.typography.regular.body4
                )
            }
        }
    }
}

@Composable
private fun ScheduleDetailContent(
    schedule: ScheduleModel,
    selectedDate: String? = null,
    modifier: Modifier = Modifier,
    contentsColor: Color = KieroTheme.colors.gray400,
    contentsStyle: TextStyle = KieroTheme.typography.regular.body4,
) {
    val isRecurring = schedule is RecurringScheduleModel

    val displayDateText = when (schedule) {
        is NormalScheduleModel -> ParentFormatters.formatDateWithDayOfWeek(schedule.date)
        is RecurringScheduleModel -> {
            val displayDate = selectedDate ?: schedule.repeatStartDate
            ParentFormatters.formatDateWithDayOfWeek(displayDate)
        }
        else -> ""
    }

    val displayTimeText = when (schedule) {
        is NormalScheduleModel -> "${schedule.startTime.take(5)}-${schedule.endTime.take(5)}"
        is RecurringScheduleModel -> "${schedule.startTime.take(5)}-${schedule.endTime.take(5)}"
        else -> ""
    }

    val repeatText = when (schedule) {
        is RecurringScheduleModel -> formatRepeatText(schedule.dayOfWeek)
        else -> "매주 반복"
    }

    Column(
        modifier = modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = displayDateText,
            style = contentsStyle,
            color = contentsColor
        )

        Text(
            text = displayTimeText,
            style = contentsStyle,
            color = contentsColor
        )

        Text(
            text = repeatText,
            style = contentsStyle,
            color = contentsColor,
            modifier = Modifier.alpha(if (isRecurring) 1f else 0f)
        )
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
                onDeleteConfirm = { _, _, _, _ -> },
                onEditClick = {},
                navigateToScheduleAdd = {},
                navigateToAlarm = {},
                isScheduleEditable = { _, _ -> false }
            )
        }
    }
}