package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentAutoInputField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = text,
        onValueChange = { newText ->
            if (newText.length <= 1000) {
                onTextChange(newText)
            }
        },
        placeholder = {
            Text(
                text = "알림장 내용을 입력하세요.",
                style = KieroTheme.typography.regular.body3.copy(
                    lineHeight = 24.sp
                ),
                color = KieroTheme.colors.gray400
            )
        },
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(
            color = KieroTheme.colors.white,
            fontSize = 16.sp,
            lineHeight = 24.sp
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
        maxLines = Int.MAX_VALUE,
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