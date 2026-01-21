package com.kiero.presentation.parent.schedule.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kiero.R
import com.kiero.core.common.extension.collectSideEffect
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.model.trigger.SnackbarState
import com.kiero.core.trigger.LocalGlobalUiEventTrigger
import com.kiero.presentation.parent.schedule.plan.component.picker.ColorPickerBottomSheet
import com.kiero.presentation.parent.schedule.plan.component.plan.ScheduleDatebar
import com.kiero.presentation.parent.schedule.plan.component.select.ColorSelectArea
import com.kiero.presentation.parent.schedule.plan.component.select.ScheduleTextField
import com.kiero.presentation.parent.schedule.plan.component.select.TimeSelectArea
import com.kiero.presentation.parent.schedule.plan.component.select.WeekSelectArea
import com.kiero.presentation.parent.schedule.plan.model.ColorType
import com.kiero.presentation.parent.schedule.plan.state.ParentPlanSideEffect
import com.kiero.presentation.parent.schedule.plan.viewmodel.ParentPlanViewModel


@Composable
fun ParentScheduleAddRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ParentPlanViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val globalTrigger = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect { effect ->
        when (effect) {
            is ParentPlanSideEffect.ShowSnackBar -> globalTrigger.showSnackbar(
                SnackbarState(effect.message)
            )

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
        startTime = uiState.startTime,
        endTime = uiState.endTime,
        onTimeSelected = viewModel::onTimeSelected,
        isPreviousEnabled = uiState.canGoToPrevious,
        onPreviousWeek = viewModel::onPreviousWeek,
        onNextWeek = viewModel::onNextWeek,
        onColorClick = { viewModel.toggleColorPicker(true) },
        onCreatePlan = { viewModel.onCreatePlanClick() }
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues),
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
            placeholder = "이름을 입력해주세요",
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