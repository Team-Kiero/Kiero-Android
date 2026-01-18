package com.kiero.presentation.signup.parent.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTextField
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentSignUpForm(
    title: String,
    placeholder: String,
    textState: TextFieldState,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    Column (
        modifier = modifier
    ) {
        Text(
            text = title,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.gray300
        )

        Spacer(modifier = Modifier.height(6.dp))

        KieroTextField(
            state = textState,
            placeholder = placeholder,
            isError = isError,
            containerColor = KieroTheme.colors.gray900,
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            onKeyboardAction = { onImeAction() }
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
                text = "특수문자, 이모지, 공백을 포함하지 않은 이름을 입력해주세요",
                style = KieroTheme.typography.regular.body5,
                color = KieroTheme.colors.point,
            )
        }
    }
}

@Preview
@Composable
private fun ParentSignUpFormPreview() {
    ParentSignUpForm(
        title = "아이의 성을 입력해주세요.",
        placeholder = "성",
        textState = TextFieldState(),
        isError = true
    )
}