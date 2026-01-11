package com.kiero.core.designsystem.component.dialog.action

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiero.core.designsystem.theme.KieroTheme

class KieroConfirmAction(
    private val text: String = "확인",
    private val onClick: () -> Unit
) : DialogAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        // Todo : 공통(kiero) 버튼으로 수정하기
        Button(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KieroTheme.colors.main
            )
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
private fun KieroConfirmActionPreview() {
    KieroTheme {
        KieroConfirmAction(
            onClick = {}
        ).invoke(modifier = Modifier)
    }
}