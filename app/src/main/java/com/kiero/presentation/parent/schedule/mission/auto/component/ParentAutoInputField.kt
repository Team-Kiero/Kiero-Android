package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentAutoInputField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "알림장 내용을 입력하세요.",
    maxLength: Int = 1000,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    textStyle: TextStyle = KieroTheme.typography.regular.body1,
    textColor: Color = KieroTheme.colors.gray400
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = text,
        onValueChange = { newText ->
            if (newText.length <= maxLength) {
                onTextChange(newText)
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

@Preview
@Composable
private fun ParentAutoInputFieldPreview() {
    KieroTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ParentAutoInputField(
                text = "",
                onTextChange = {}
            )
        }
    }
}

@Preview
@Composable
private fun ParentAutoInputFieldWithTextPreview() {
    KieroTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ParentAutoInputField(
                text = "내일은 독서록을 가져오세요. 수학 익힘책 30쪽까지 풀어오세요.",
                onTextChange = {}
            )
        }
    }
}

@Preview
@Composable
private fun ParentAutoInputFieldMissionNamePreview() {
    KieroTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ParentAutoInputField(
                text = "독서록 챙기기",
                onTextChange = {},
                placeholder = "미션 이름을 입력해주세요.",
                maxLength = 15,
                singleLine = true
            )
        }
    }
}