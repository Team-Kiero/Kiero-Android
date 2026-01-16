package com.kiero.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroTextField(
    state: TextFieldState,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null,
    supportingIcon: Int? = null,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    shape: Shape = RoundedCornerShape(15.dp),
    containerColor: Color = KieroTheme.colors.black,
    textColor: Color = KieroTheme.colors.white,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        isError -> KieroTheme.colors.point
        isFocused -> KieroTheme.colors.gray100
        else -> Color.Transparent
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        BasicTextField(
            state = state,
            enabled = enabled,
            readOnly = readOnly,
            inputTransformation = inputTransformation,
            outputTransformation = outputTransformation,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = lineLimits,
            textStyle = KieroTheme.typography.regular.body4.copy(color = textColor),
            cursorBrush = SolidColor(if (isError) KieroTheme.colors.point else KieroTheme.colors.white),
            interactionSource = interactionSource,
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = containerColor,
                            shape = shape
                        )
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = shape
                        )
                        .padding(horizontal = 13.dp, vertical = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = KieroTheme.typography.regular.body4,
                            color = KieroTheme.colors.gray700
                        )
                    }
                    innerTextField()
                }
            }
        )

        supportingText?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                supportingIcon?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_info_error),
                        contentDescription = null,
                        tint = KieroTheme.colors.point,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Text(
                    text = it,
                    style = KieroTheme.typography.regular.body5,
                    color = KieroTheme.colors.point,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KieroTextFieldPreviewDefault() {
    KieroTheme {
        KieroTextField(
            state = rememberTextFieldState(),
            placeholder = "텍스트를 입력하세요",
        )
    }
}

