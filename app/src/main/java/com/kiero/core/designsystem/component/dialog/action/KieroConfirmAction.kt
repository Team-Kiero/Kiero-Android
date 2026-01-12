package com.kiero.core.designsystem.component.dialog.action

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.core.designsystem.component.button.KieroButtonSmall
import com.kiero.core.designsystem.theme.KieroTheme

class KieroConfirmAction(
    private val text: String = "확인",
    private val onClick: () -> Unit
) : DialogAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        KieroButtonSmall(
            text = text,
            onClick = onClick,
            modifier = modifier,
            containerColor = KieroTheme.colors.main,
            contentColor = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KieroConfirmActionPreview() {
    KieroTheme {
        KieroConfirmAction(
            onClick = {}
        ).invoke(modifier = Modifier)
    }
}