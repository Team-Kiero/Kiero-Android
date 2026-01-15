package com.kiero.presentation.parent.schedule.mission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.component.datepicker.component.CalendarBottomSheet
import com.kiero.presentation.parent.schedule.mission.component.missionadd.MissionAwardInfo
import com.kiero.presentation.parent.schedule.mission.component.missionadd.MissionAwardSelect
import com.kiero.presentation.parent.schedule.mission.component.missionmain.MissionCalendar
import com.kiero.presentation.parent.schedule.mission.model.MissionAwardDefaults
import com.kiero.presentation.parent.schedule.plan.component.select.ScheduleTextField
import kotlinx.coroutines.flow.debounce


@Composable
fun ParentAddMissionRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    val missionNameState = rememberTextFieldState()
    val awardTextFieldState = rememberTextFieldState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var currentAwardValue by rememberSaveable { mutableIntStateOf(MissionAwardDefaults.DEFAULT_AWARD) }

    LaunchedEffect(awardTextFieldState) {
        snapshotFlow { awardTextFieldState.text.toString() }
            .debounce(500)
            .collect { text ->
                if (text.isNotEmpty()) {
                    val parsed = text.toIntOrNull() ?: MissionAwardDefaults.DEFAULT_AWARD
                    val newValue = MissionAwardDefaults.constrainValue(parsed)
                    if (newValue != currentAwardValue) {
                        currentAwardValue = newValue
                        if (parsed != newValue) {
                            awardTextFieldState.edit {
                                replace(0, length, newValue.toString())
                            }
                        }
                    }
                }
            }
    }

    LaunchedEffect(currentAwardValue) {
        if (awardTextFieldState.text.toString().toIntOrNull() != currentAwardValue) {
            awardTextFieldState.edit {
                replace(0, length, currentAwardValue.toString())
            }
        }
    }

    ParentAddMissionScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
        missionNameState = missionNameState,
        awardTextFieldState = awardTextFieldState,
        showBottomSheet = showBottomSheet,
        onDateClick = { showBottomSheet = true },
        onDismissBottomSheet = { showBottomSheet = false },
        onAwardClick = { change ->
            currentAwardValue = MissionAwardDefaults.applyChange(currentAwardValue, change)
        }
    )
}

@Composable
fun ParentAddMissionScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    missionNameState: TextFieldState,
    awardTextFieldState: TextFieldState,
    showBottomSheet: Boolean,
    onDateClick: () -> Unit,
    onDismissBottomSheet: () -> Unit,
    onAwardClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        KieroTopbar(
            title = "미션추가",
            leftIconRes = R.drawable.ic_close_light,
            rightIconRes = R.drawable.ic_check,
            leftIconClick = navigateUp,
            rightIconClick = {}
        )

        ScheduleTextField(
            state = missionNameState,
            placeholder = "미션 이름을 입력해주세요."
        )

        MissionCalendar(
            onDateClick = onDateClick,
            dateText = "2025.12.26.(금)"
        )

        MissionAwardInfo()

        MissionAwardSelect(
            textFieldState = awardTextFieldState,
            onAwardClick = onAwardClick,
        )

        if (showBottomSheet) {
            CalendarBottomSheet(
                onDismissRequest = onDismissBottomSheet
            )
        }
    }
}

@Preview
@Composable
private fun ParentAddMissionScreenPreview() {
    KieroTheme {
        ParentAddMissionScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            missionNameState = TextFieldState(),
            awardTextFieldState = TextFieldState(),
            showBottomSheet = false,
            onDateClick = {},
            onDismissBottomSheet = {},
            onAwardClick = {}
        )
    }
}