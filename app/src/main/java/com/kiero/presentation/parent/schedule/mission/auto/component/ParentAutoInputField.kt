package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentAutoInputField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSelectionChange: ((TextRange) -> Unit)? = null,
    placeholder: String = "알림장 내용을 입력하세요.",
    maxLength: Int = 1000,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    textStyle: TextStyle = KieroTheme.typography.regular.body1,
    textColor: Color = KieroTheme.colors.gray400
) {
    val focusManager = LocalFocusManager.current
    var selection by remember { mutableStateOf(TextRange(text.length)) }

    val textFieldValue = TextFieldValue(
        text = text,
        selection = selection
    )
    TextField(
        value = textFieldValue, // String 대신 TextFieldValue 사용
        onValueChange = { newTextFieldValue ->
            if (newTextFieldValue.text.length <= maxLength) {
                if (text != newTextFieldValue.text) {
                    onTextChange(newTextFieldValue.text)
                }
                selection = newTextFieldValue.selection
                onSelectionChange?.invoke(newTextFieldValue.selection)
            }
        },
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle,
                color = textColor
            )
        },
        modifier = modifier.fillMaxWidth(),
        textStyle = textStyle.copy(
            color = textColor
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = KieroTheme.colors.gray900,
            unfocusedContainerColor = KieroTheme.colors.gray900,
            disabledContainerColor = KieroTheme.colors.gray900,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = KieroTheme.colors.white
        ),
        shape = RoundedCornerShape(15.dp),
        maxLines = maxLines,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}
