package com.kiero.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KieroButtonSmall(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = KieroTheme.colors.main,
    contentColor: Color = KieroTheme.colors.black,
    isEnabled: Boolean = true
) {
    Surface(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (isEnabled) containerColor else KieroTheme.colors.gray300,
        contentColor = if (isEnabled) contentColor else KieroTheme.colors.gray600
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                style = KieroTheme.typography.semiBold.title4
            )
        }
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
            Column {
                Text("Primary",
                    style = KieroTheme.typography.regular.body4)
                KieroButtonSmall(
                    text = "확인", onClick = {})
            }

            Column {
                Text("Secondary", style = KieroTheme.typography.regular.body4)
                KieroButtonSmall(
                    text = "취소",
                    onClick = {},
                    containerColor = KieroTheme.colors.white,
                    contentColor = KieroTheme.colors.gray800
                )
            }
        }
    }
}