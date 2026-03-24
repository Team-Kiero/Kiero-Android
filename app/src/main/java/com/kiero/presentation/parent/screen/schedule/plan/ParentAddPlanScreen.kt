package com.kiero.presentation.parent.screen.schedule.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.dialog.KieroDialog
import com.kiero.core.designsystem.component.dialog.action.KieroCancelAction
import com.kiero.core.designsystem.component.dialog.action.KieroConfirmAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.screen.schedule.plan.component.picker.ColorPickerBottomSheet
import com.kiero.presentation.parent.screen.schedule.plan.component.plan.ScheduleDatebar
import com.kiero.presentation.parent.screen.schedule.plan.component.select.ColorSelectArea
import com.kiero.presentation.parent.screen.schedule.plan.component.select.ScheduleTextField
import com.kiero.presentation.parent.screen.schedule.plan.component.select.TimeSelectArea
import com.kiero.presentation.parent.screen.schedule.plan.component.select.WeekSelectArea
import com.kiero.presentation.parent.screen.schedule.plan.model.ColorType
import com.kiero.presentation.parent.screen.schedule.plan.state.ParentPlanSideEffect
import com.kiero.presentation.parent.screen.schedule.plan.viewmodel.ParentPlanViewModel

private enum class EditRepeatOption { THIS_ONLY, INCLUDE_FOLLOWING }

@Composable
fun ParentScheduleAddRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentPlanViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current
    val focusManager = LocalFocusManager.current

    var showEditDialog by remember { mutableStateOf(false) }
    var editRepeatOption by remember { mutableStateOf(EditRepeatOption.THIS_ONLY) }

    viewModel.sideEffect.collectSideEffect { effect ->
        when (effect) {
            is ParentPlanSideEffect.ShowSnackBar -> {
                focusManager.clearFocus()
                globalTrigger.showSnackbar(SnackbarState(effect.message))
            }
            is ParentPlanSideEffect.navigateUp -> {
                navigateUp()
            }
        }
    }

    ScheduleAddScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        textState = viewModel.textState,
        selectedDays = uiState.selectedDays,
        onDayClick = viewModel::onDayClick,
        onAllDaysSelect = viewModel::onAllDaysSelect,
        isRecurring = uiState.isRecurring,
        onRecurringToggle = viewModel::onRecurringToggle,
        selectedColorType = uiState.selectedColorType,
        dateRangeText = uiState.dateRangeText,
        startTime = uiState.displayStartTime,
        endTime = uiState.displayEndTime,
        onTimeSelected = viewModel::onTimeSelected,
        isPreviousEnabled = uiState.canGoToPrevious,
        onPreviousWeek = viewModel::onPreviousWeek,
        onNextWeek = viewModel::onNextWeek,
        onColorClick = { viewModel.toggleColorPicker(true) },
        onCreatePlan = {
            if (viewModel.shouldShowEditDialog()) {
                editRepeatOption = EditRepeatOption.THIS_ONLY
                showEditDialog = true
            } else {
                viewModel.onCreatePlanClick()
            }
        }
    )

    if (uiState.showColorPicker) {
        ColorPickerBottomSheet(
            selectedColorType = uiState.selectedColorType,
            onColorConfirmed = { confirmedColorType ->
                viewModel.onColorSelected(confirmedColorType)
            },
            onDismissRequest = { viewModel.toggleColorPicker(false) }
        )
    }

    if (showEditDialog) {
        KieroDialog(
            onDismiss = { showEditDialog = false },
            title = viewModel.textState.text.toString(),
            subDescription = null,
            cancelAction = KieroCancelAction(
                text = "취소",
                onClick = { showEditDialog = false }
            ),
            confirmAction = KieroConfirmAction(
                text = "확인",
                onClick = {
                    showEditDialog = false
                    val includeFollowing = if (viewModel.isEditRecurring) {
                        editRepeatOption == EditRepeatOption.INCLUDE_FOLLOWING
                    } else null

                    viewModel.onCreatePlanClick(isIncludeFollowing = includeFollowing)
                }
            ),
            content = {
                Box(
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val pullUpHeight = 35.dp.roundToPx()

                        layout(placeable.width, (placeable.height - pullUpHeight).coerceAtLeast(0)) {
                            placeable.placeRelative(0, 0)
                        }
                    }
                ) {
                    ScheduleEditDialogContent(
                        selectedOption = editRepeatOption,
                        onOptionClick = { editRepeatOption = it },
                        isRecurring = viewModel.isEditRecurring,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        )
    }
}

@Composable
private fun ScheduleEditDialogContent(
    selectedOption: EditRepeatOption,
    onOptionClick: (EditRepeatOption) -> Unit,
    isRecurring: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "저장하시겠습니까?",
            color = KieroTheme.colors.gray100,
            style = KieroTheme.typography.regular.body3,
            textAlign = TextAlign.Center,
        )

        if (isRecurring) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .noRippleClickable { onOptionClick(EditRepeatOption.THIS_ONLY) }
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val iconResThis = if (selectedOption == EditRepeatOption.THIS_ONLY) {
                        R.drawable.ic_parent_addschedule_check_on
                    } else {
                        R.drawable.ic_parent_addschedule_check_off
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = iconResThis),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Text(
                        text = "이번 일정만 포함",
                        color = KieroTheme.colors.gray400,
                        style = KieroTheme.typography.regular.body4
                    )
                }

                Row(
                    modifier = Modifier
                        .noRippleClickable { onOptionClick(EditRepeatOption.INCLUDE_FOLLOWING) }
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val iconResFollowing = if (selectedOption == EditRepeatOption.INCLUDE_FOLLOWING) {
                        R.drawable.ic_parent_addschedule_check_on
                    } else {
                        R.drawable.ic_parent_addschedule_check_off
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = iconResFollowing),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Text(
                        text = "이후 일정 포함",
                        color = KieroTheme.colors.gray400,
                        style = KieroTheme.typography.regular.body4
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleAddScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    textState: TextFieldState,
    selectedDays: Set<Int>,
    onAllDaysSelect: (Boolean) -> Unit,
    isRecurring: Boolean,
    onDayClick: (Int) -> Unit,
    onRecurringToggle: () -> Unit,
    selectedColorType: ColorType?,
    dateRangeText: String,
    startTime: String,
    endTime: String,
    onTimeSelected: (Boolean, String) -> Unit,
    isPreviousEnabled: Boolean,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onColorClick: () -> Unit,
    onCreatePlan: () -> Unit,
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
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        KieroTopbar(
            title = "일정 추가",
            leftIconRes = R.drawable.ic_close_light,
            rightIconRes = R.drawable.ic_check,
            leftIconClick = navigateUp,
            rightIconClick = onCreatePlan
        )

        ScheduleDatebar(
            date = dateRangeText,
            onPreviousClick = onPreviousWeek,
            onNextClick = onNextWeek,
            isPreviousEnabled = isPreviousEnabled
        )

        ScheduleTextField(
            state = textState,
            placeholder = "일정 이름을 입력해주세요",
        )

        WeekSelectArea(
            selectedDays = selectedDays,
            isRecurring = isRecurring,
            onDayClick = onDayClick,
            onAllDaysSelect = onAllDaysSelect,
            onRecurringToggle = onRecurringToggle
        )

        TimeSelectArea(
            startTime = startTime,
            endTime = endTime,
            onTimeSelected = onTimeSelected
        )

        Spacer(modifier = Modifier.height(8.dp))

        ColorSelectArea(
            selectedColorType = selectedColorType,
            onColorClick = onColorClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ParentAddPlanScreenPreview() {
    KieroTheme {
        ParentScheduleAddRoute(
            paddingValues = PaddingValues(),
            navigateUp = {}
        )
    }
}