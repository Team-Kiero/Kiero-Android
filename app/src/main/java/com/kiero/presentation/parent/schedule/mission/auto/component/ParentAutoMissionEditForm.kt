package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.model.MissionUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ParentAutoMissionEditForm(
    mission: MissionUiModel,
    selectedDate: LocalDate?,
    onMissionNameChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onRewardClick: (Int) -> Unit,
    awardTextFieldState: TextFieldState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val dateToDisplay = remember(selectedDate, mission.dueAt) {
        val targetDate = selectedDate ?: mission.dueAt
        targetDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.(E)", Locale.KOREA))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(
                top = 12.dp,
                start = 14.dp,
                end = 14.dp,
                bottom = 12.dp
            )
        ) {
            ParentAutoInputField(
                text = mission.name,
                onTextChange = onMissionNameChange,
                placeholder = "미션 이름을 입력해주세요.",
                maxLength = 15,
                singleLine = true
            )

            ParentAutoMissionCalendar(
                dateText = dateToDisplay,
                onDateClick = onDateClick
            )

            ParentAutoMissionAwardInfo()

            Spacer(Modifier.height(12.dp))

            ParentAutoMissionAwardSelect(
                textFieldState = awardTextFieldState,
                onAwardClick = onRewardClick
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .padding(bottom = 36.dp)
        ) {
            SnackbarHost(hostState = snackbarHostState) { data ->
                ParentLocalKieroSnackbar(
                    message = data.visuals.message,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}