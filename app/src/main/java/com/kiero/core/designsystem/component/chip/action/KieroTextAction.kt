package com.kiero.core.designsystem.component.chip.action

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

class KieroTextAction(
    private val text: String,
    private val onClick: () -> Unit
) : ChipAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        Text(
            text = text,
            style = KieroTheme.typography.regular.body5,
            color = KieroTheme.colors.white,
            modifier = modifier
                .noRippleClickable(onClick = onClick)
        )
    }
}

@Preview
@Composable
private fun KieroTextActionPreview() {
    KieroTheme {
        KieroTextAction(
            text = "꾸비",
            onClick = {},
        ).invoke(modifier = Modifier)
    }
}