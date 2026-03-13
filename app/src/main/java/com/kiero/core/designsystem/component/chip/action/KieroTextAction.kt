package com.kiero.core.designsystem.component.chip.action

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

enum class KieroTextColor { WHITE, MAIN, GRAY500 }

class KieroTextAction(
    private val text: String,
    private val textColor: KieroTextColor = KieroTextColor.WHITE,
    private val onClick: () -> Unit
) : ChipAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        val color = when (textColor) {
            KieroTextColor.WHITE -> KieroTheme.colors.white
            KieroTextColor.MAIN -> KieroTheme.colors.main
            KieroTextColor.GRAY500 -> KieroTheme.colors.gray500
        }

        Text(
            text = text,
            style = KieroTheme.typography.regular.body6,
            color = color,
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
