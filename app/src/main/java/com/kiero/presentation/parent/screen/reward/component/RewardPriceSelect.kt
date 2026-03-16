package com.kiero.presentation.parent.screen.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.screen.reward.model.RewardPriceDefaults

@Composable
fun RewardPriceSelect(
    textFieldState: TextFieldState,
    onPriceClick: (Int) -> Unit,
    onValueAdjust: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RewardPriceDefaults.PRESET_VALUES.forEachIndexed { index, presetValue ->
            if (index == 2) {
                RewardPriceTextField(
                    state = textFieldState,
                    onValueAdjust = onValueAdjust,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 7.dp)
                )
            }

            RewardPriceButton(
                text = presetValue.displayText,
                onClick = { onPriceClick(presetValue.value) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
@Composable
private fun RewardPriceTextField(
    state: TextFieldState,
    onValueAdjust: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        state = state,
        modifier = modifier.onFocusChanged { focusState ->
            isFocused = focusState.isFocused
            if (!focusState.isFocused) {
                val current = state.text.toString().toIntOrNull() ?: 0
                if (current > RewardPriceDefaults.MAX_PRICE) {
                    onValueAdjust(RewardPriceDefaults.MAX_PRICE)
                }
            }
        },
        inputTransformation = InputTransformation {
            if (!asCharSequence().all { it.isDigit() }) revertAllChanges()
            if (asCharSequence().length > 3) revertAllChanges()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = KieroTheme.typography.semiBold.title3.copy(
            color = KieroTheme.colors.white,
            textAlign = TextAlign.Center
        ),
        cursorBrush = SolidColor(KieroTheme.colors.white),
        decorator = { innerTextField ->
            Box(contentAlignment = Alignment.Center) {
                if (state.text.isEmpty() && !isFocused) {
                    Text(
                        text = "0",
                        style = KieroTheme.typography.semiBold.title3,
                        color = KieroTheme.colors.gray500,
                    )
                }
                innerTextField()
            }
        }
    )
}


@Composable
private fun RewardPriceButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(15.dp),
            )
            .noRippleClickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = KieroTheme.colors.gray500,
            style = KieroTheme.typography.semiBold.title3,
        )
    }
}

@Preview
@Composable
private fun RewardPriceSelectPreview() {
    KieroTheme {
        RewardPriceSelect(
            textFieldState = rememberTextFieldState(),
            onPriceClick = {},
            onValueAdjust = {}
        )
    }
}