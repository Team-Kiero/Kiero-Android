package com.kiero.presentation.parent.screen.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun RewardNameTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        state = state,
        inputTransformation = InputTransformation.maxLength(15),
        textStyle = KieroTheme.typography.regular.body1.copy(color = KieroTheme.colors.white),
        cursorBrush = SolidColor(KieroTheme.colors.white),
        interactionSource = interactionSource,
        modifier = modifier,
        decorator = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Unspecified)
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (state.text.isEmpty()) {
                    Text(
                        text = "보상 이름을 입력해주세요",
                        style = KieroTheme.typography.regular.body1,
                        color = KieroTheme.colors.gray500,
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun RewardNameTextFieldPreview() {
    KieroTheme {
        RewardNameTextField(state = rememberTextFieldState())
    }
}