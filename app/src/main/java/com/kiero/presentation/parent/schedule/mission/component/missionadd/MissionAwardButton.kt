package com.kiero.presentation.parent.schedule.mission.component.missionadd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun MissionAwardSelect(
    textFieldState: TextFieldState,
    onAwardClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MissionAwardDefaults.PRESET_VALUES.forEachIndexed { index, presetValue ->
            if (index == 2) {
                MissionTextField(
                    state = textFieldState,
                    placeholder = "0",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 7.dp)
                )
            }

            MissionAwardButton(
                awardText = presetValue.displayText,
                onAwardClick = { onAwardClick(presetValue.value) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MissionAwardButton(
    awardText: String,
    onAwardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(15.dp)
            )
            .noRippleClickable(onClick = onAwardClick)
            .padding(horizontal = 14.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = awardText,
            color = KieroTheme.colors.gray500,
            style = KieroTheme.typography.semiBold.title3,
        )
    }
}

@Preview
@Composable
private fun MissionAwardPreview() {
    KieroTheme {
        Column {
            MissionAwardSelect(
                textFieldState = TextFieldState(),
                onAwardClick = {},
            )
        }
    }
}