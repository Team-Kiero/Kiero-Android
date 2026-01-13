package com.kiero.core.designsystem.component.dialog.action

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.component.button.KieroButtonSmall
import com.kiero.core.designsystem.theme.KieroTheme

class KieroCancelAction(
    private val text: String = "취소",
    private val onClick: () -> Unit
) : DialogAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        KieroButtonSmall(
            text = text,
            onClick = onClick,
            modifier = modifier,
            containerColor = KieroTheme.colors.gray800,
            contentColor = KieroTheme.colors.white
        )
    }
}

@Preview
@Composable
private fun KieroCancelActionPreview() {
    KieroTheme {
        KieroCancelAction(
            onClick = {}
        ).invoke(modifier = Modifier)
    }
}