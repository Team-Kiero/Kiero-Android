package com.kiero.presentation.auth.parent.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme


@Composable
fun KakaoLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row (
        modifier = modifier
            .background(
                color = KieroTheme.colors.yellow,
                shape = RoundedCornerShape(10.dp)
            )
            .noRippleClickable(onClick = onClick)
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_kakao_login),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(start = 40.dp)
        )

        Text(
            text = "카카오 계정으로 로그인",
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.black,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
                .padding(end = 25.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KakaoLoginButtonPreview() {
    KieroTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            KakaoLoginButton(
                onClick = {},
            )
        }
    }
}