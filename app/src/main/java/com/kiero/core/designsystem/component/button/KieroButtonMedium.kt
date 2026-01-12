package com.kiero.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.core.designsystem.theme.defaultKieroColors


@Composable
fun KieroButtonMedium(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    containerColor: Color = KieroTheme.colors.main,
    contentColor: Color = KieroTheme.colors.black,
    isEnabled: Boolean = true,
) {
    Surface(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier
            .fillMaxWidth()
            .height(49.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (isEnabled) containerColor else KieroTheme.colors.gray300,
        contentColor = if (isEnabled) contentColor else KieroTheme.colors.gray600
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 13.dp, horizontal = 134.5.dp), // 내부 패딩으로 유연성 확보
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon, // ImageVector로 변경
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = KieroTheme.typography.semiBold.title3
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun KieroButtonMediumPreview() {
    KieroTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KieroButtonMedium(
                text = "시작하기",
                onClick = { }
            )
            KieroButtonMedium(
                text = "일정 추가하기",
                leadingIcon = ImageVector.vectorResource(id = com.kiero.R.drawable.ic_kid_camera),
                containerColor = defaultKieroColors.gray900,
                contentColor = defaultKieroColors.white,
                onClick = { }
            )
            KieroButtonMedium(
                text = "비활성화 버튼",
                isEnabled = false,
                onClick = { }
            )
        }
    }
}