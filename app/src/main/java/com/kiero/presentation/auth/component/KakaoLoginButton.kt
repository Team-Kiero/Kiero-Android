package com.kiero.presentation.auth.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme



@Composable
fun KakaoLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier
            .width(328.dp)
            .height(45.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFAE100),
            contentColor = KieroTheme.colors.black
        ),
        contentPadding = PaddingValues(top = 5.dp, end = 21.dp, bottom = 5.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Icon(
            painter = painterResource(id = com.kiero.R.drawable.ic_kakao_login),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = KieroTheme.colors.black
        )

        Spacer(modifier = Modifier.width(41.dp))

        Text(
            text = if (isLoading) "로그인 중..." else "카카오톡 로그인",
            style = KieroTheme.typography.semiBold.title3,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true, name = "카카오 로그인 - 기본")
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
                isLoading = false
            )
        }
    }
}

@Preview(showBackground = true, name = "카카오 로그인 - 로딩 중")
@Composable
private fun KakaoLoginButtonLoadingPreview() {
    KieroTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            KakaoLoginButton(
                onClick = {},
                isLoading = true
            )
        }
    }
}