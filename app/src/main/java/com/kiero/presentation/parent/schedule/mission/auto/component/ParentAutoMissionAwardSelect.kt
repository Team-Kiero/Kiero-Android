package com.kiero.presentation.parent.schedule.mission.auto.component

import com.kiero.presentation.parent.schedule.mission.component.missionadd.MissionTextField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.component.model.MissionAwardDefaults

@Composable
fun ParentAutoMissionAwardSelect(
    textFieldState: TextFieldState,
    onAwardClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MissionAwardDefaults.PRESET_VALUES.forEachIndexed { index, presetValue ->
            if (index == 2) {
                MissionTextField(
                    state = textFieldState,
                    placeholder = textFieldState.text.toString().ifEmpty {
                        MissionAwardDefaults.DEFAULT_AWARD.toString()
                    },
                    modifier = Modifier
                        .weight(0.5f)
                )
            }

            ParentAutoMissionAwardButton(
                awardText = presetValue.displayText,
                onAwardClick = { onAwardClick(presetValue.value) },
                modifier = Modifier.weight(0.5f)
            )
        }
    }
}

@Composable
private fun ParentAutoMissionAwardButton(
    awardText: String,
    onAwardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .background(
                color = KieroTheme.colors.gray800,
                shape = RoundedCornerShape(15.dp)
            )
            .noRippleClickable(onClick = onAwardClick),
            //.padding(horizontal = 14.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = awardText,
            color = KieroTheme.colors.white,
            style = KieroTheme.typography.semiBold.title3,
            maxLines = 1
        )
    }
}

@Preview
@Composable
private fun ParentAutoMissionAwardPreview() {
    KieroTheme {
        Column {
            ParentAutoMissionAwardSelect(
                textFieldState = TextFieldState(),
                onAwardClick = {},
            )
        }
    }
}