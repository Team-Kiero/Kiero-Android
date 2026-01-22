package com.kiero.presentation.auth.kid.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.util.MaxLengthInputTransformation
import com.kiero.core.designsystem.component.KieroTextField
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidInputField(
    fieldTitle: String,
    fieldInputText: String,
    fieldState: TextFieldState,
    onImeAction: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    inputTransformation : InputTransformation? = null,
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
            containerColor = KieroTheme.colors.gray900,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                autoCorrectEnabled = false,
                imeAction = imeAction
            ),
            onKeyboardAction = { performDefaultAction ->
                onImeAction()
            },
            inputTransformation = inputTransformation
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .alpha(if (isError) 1f else 0f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_info_error),
                contentDescription = null,
                tint = KieroTheme.colors.point,
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "특수문자나 이모지를 포함하지 않은 이름을 입력해주세요",
                style = KieroTheme.typography.regular.body5,
                color = KieroTheme.colors.point,
            )
        }
    }
}

@Preview
@Composable
private fun KieroInputField() {
    KieroTheme {
        KidInputField(
            fieldTitle = "성",
            fieldInputText = "성을 입력해줘!",
            fieldState = TextFieldState(),
            isError = true,
            onImeAction = {},
            inputTransformation = MaxLengthInputTransformation(10)
        )
    }
}
