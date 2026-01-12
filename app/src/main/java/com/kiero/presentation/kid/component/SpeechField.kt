package com.kiero.presentation.kid.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun SpeechField(
    name: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.padding(top = 17.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background((KieroTheme.colors.gray900), shape = RoundedCornerShape(15.dp))
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            content()
        }

        Text(
            text = name,
            modifier = Modifier
                .offset(x = 18.dp, y = (-17).dp)
                .background((KieroTheme.colors.main), shape = RoundedCornerShape(4.dp))
                .padding(vertical = 2.dp, horizontal = 10.dp)
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun SpeechFieldPreview() {
    SpeechField(
        name = "이름"
    ) {
        Text("첫번째 줄 입니다", color = KieroTheme.colors.gray300)
        Text("두번째 줄 입니다", color = KieroTheme.colors.gray300)

        Text(
            text = buildAnnotatedString {
                append("덕분에 ")
                withStyle(style = SpanStyle(color = KieroTheme.colors.main)) {
                    append("영웅의 불꽃")
                }
                append(" 이 커졌어!")
            },
            color = KieroTheme.colors.gray300
        )

        Text(
            text = buildAnnotatedString {
                append("선물로 금화 ")
                withStyle(style = SpanStyle(color = KieroTheme.colors.main)) {
                    append("10")
                }
                append(" 개를 줄게")
            },
            color = KieroTheme.colors.gray300
        )
    }
}