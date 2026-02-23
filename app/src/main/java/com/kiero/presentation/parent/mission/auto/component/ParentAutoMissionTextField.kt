package com.kiero.presentation.parent.mission.auto.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentAutoMissionTextField(
    state: TextFieldState,
    placeholder: String,
    modifier: Modifier = Modifier,
    onFocusLost: () -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    ),
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    textColor: Color = KieroTheme.colors.white,
    maxLength: Int = 3,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val borderColor = KieroTheme.colors.gray600

    val defaultKeyboardAction = KeyboardActionHandler {
        if (state.text.isEmpty()) {
            state.edit {
                replace(0, 0, "20")
            }
        }
        onFocusLost()
        focusManager.clearFocus()
    }

    BasicTextField(
        state = state,
        enabled = enabled,
        readOnly = readOnly,
        inputTransformation = inputTransformation ?: InputTransformation.maxLength(maxLength),
        outputTransformation = outputTransformation,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction ?: defaultKeyboardAction,
        lineLimits = lineLimits,
        textStyle = KieroTheme.typography.semiBold.title3.copy(
            color = textColor,
            textAlign = TextAlign.Center
        ),
        cursorBrush = SolidColor(KieroTheme.colors.white),
        interactionSource = interactionSource,
        modifier = modifier.wrapContentWidth(),
        decorator = { innerTextField ->
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .background(color = Color.Transparent)
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = KieroTheme.typography.semiBold.title3,
                        color = KieroTheme.colors.white,
                        textAlign = TextAlign.Center
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun MissionTextFieldPreviewDefault() {
    KieroTheme {
        ParentAutoMissionTextField(
            state = rememberTextFieldState(),
            placeholder = "20",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF232428)
@Composable
private fun MissionTextFieldPreviewWithText() {
    KieroTheme {
        ParentAutoMissionTextField(
            state = rememberTextFieldState("50"),
            placeholder = "20",
        )
    }
}