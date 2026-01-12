package com.kiero.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiero.core.designsystem.component.button.model.KieroButtonColors
import com.kiero.core.designsystem.theme.Gray800
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.designsystem.theme.White

@Composable
fun KieroButtonSmall(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: KieroButtonColors = KieroButtonColors(),
    isEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(70.dp)
            .height(40.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor(),
            contentColor = colors.contentColor()
        ),
        // 버튼이 작으므로 내부 패딩을 최소화하여 글자가 잘리지 않게 함
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true, name = "Small Button Variations")
@Composable
fun KieroButtonSmallPreview() {
    KieroTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. 기본 확인 버튼 (Main 컬러)
            Column {
                KieroButtonSmall(
                    text = "확인",
                    onClick = {}
                )
            }
            // 2. 보조 버튼 (Gray 컬러 - 보통 '취소'에 사용)
            Column {
                KieroButtonSmall(
                    text = "취소",
                    onClick = {},
                    colors = KieroButtonColors(
                        containerColor = White, // 직접 정의하신 Gray200 사용
                        contentColor = Gray800
                    )
                )
            }
        }
    }
}