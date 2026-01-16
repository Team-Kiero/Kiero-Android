package com.kiero.presentation.parent.schedule.mission.auto.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.KieroTextField
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentAutoInputField(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    KieroTextField(
        state = state,
        placeholder = "알림장 내용을 입력하세요.",
        modifier = modifier.fillMaxWidth(),
        lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 1),
        containerColor = KieroTheme.colors.gray900,
        inputTransformation = InputTransformation.maxLength(1000)
    )
}
@Preview(showBackground = true)
@Composable
private fun ParentAutoInputFieldPreview() {
    KieroTheme {
        val state = rememberTextFieldState("")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ParentAutoInputField(
                state = state
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ParentAutoInputFieldWithTextPreview() {
    KieroTheme {
        val state = rememberTextFieldState("내일은 독서록을 가져오세요. 수학 익힘책 30쪽까지 풀어오세요.")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ParentAutoInputField(
                state = state
            )
        }
    }
}