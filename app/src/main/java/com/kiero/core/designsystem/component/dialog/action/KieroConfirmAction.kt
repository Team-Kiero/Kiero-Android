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
import com.kiero.core.designsystem.component.button.KieroButtonSmall
import com.kiero.core.designsystem.component.button.model.KieroButtonColors
import com.kiero.core.designsystem.theme.KieroTheme

class KieroConfirmAction(
    private val text: String = "확인",
    private val onClick: () -> Unit
) : DialogAction {
    @Composable
    override fun invoke(modifier: Modifier) {
        // 공통 컴포넌트 KieroButtonSmall로 교체
        KieroButtonSmall(
            text = text,
            onClick = onClick,
            modifier = modifier,
            // 필요하다면 여기서 직접 colors를 주입해 다이얼로그 전용 색상을 쓸 수 있습니다.
            colors = KieroButtonColors(
                containerColor = KieroTheme.colors.main,
                contentColor = Color.Black
            )
        )
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