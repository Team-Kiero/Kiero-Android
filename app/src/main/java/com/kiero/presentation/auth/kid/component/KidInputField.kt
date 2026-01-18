package com.kiero.presentation.auth.kid.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.component.KieroTextField
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidInputField(
    fieldTitle: String,
    fieldInputText: String,
    fieldState: TextFieldState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = KieroTheme.colors.black)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp)

    ) {
        Text(
            text = fieldTitle,
            color = KieroTheme.colors.gray300,
            style = KieroTheme.typography.regular.body3
        )

        KieroTextField(
            state = fieldState,
            placeholder = fieldInputText,
            containerColor = KieroTheme.colors.gray900
        )
    }
}

@Preview
@Composable
private fun KieroInputField() {
    KieroTheme {
        KidInputField(
            fieldTitle = "성",
            fieldInputText = "성을 입력해줘!",
            fieldState = TextFieldState()
        )
    }
}