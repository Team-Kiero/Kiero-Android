package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.model.MissionUiModel
import com.kiero.presentation.parent.schedule.mission.auto.viewmodel.AutoMissionViewModel
import java.time.format.DateTimeFormatter


@Composable
fun ParentAutoMissionEditForm(
    mission: MissionUiModel,
    selectedDate: String?,
    viewModel: AutoMissionViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(
                top = 12.dp,
                start = 14.dp,
                end = 14.dp,
                bottom = 12.dp
            ),
    ) {
        ParentAutoInputField(
            text = mission.name,
            onTextChange = { viewModel.updateMissionName(it) },
            placeholder = "미션 이름을 입력해주세요.",
            maxLength = 15,
            singleLine = true
        )

        ParentAutoMissionCalendar(
            dateText = selectedDate ?: mission.dueAt.format(
                DateTimeFormatter.ofPattern("yyyy.MM.dd.(E)")
            ),
            onDateClick = { viewModel.onDateClick() }
        )

        ParentAutoMissionAwardInfo()

        Spacer(Modifier.height(12.dp))

        ParentAutoMissionAwardSelect(
            textFieldState = viewModel.awardTextFieldState,
            onAwardClick = { viewModel.updateMissionReward(it) }
        )
    }
}