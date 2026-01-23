package com.kiero.presentation.kid.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidSpeechField(
    modifier: Modifier = Modifier,
    name: String = "꾸비",
    buttonText: String = "다음 여정으로",
    isVisibleButton: Boolean = false,
    nextButtonColor: Color = KieroTheme.colors.gray600,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 34.dp)
                .background(
                    color = KieroTheme.colors.gray900,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            content()
        }

        if (isVisibleButton) {
            NextButton(
                onClick = onClick,
                text = buttonText,
                color = nextButtonColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }

        Text(
            text = name,
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.black,
            modifier = Modifier
                .padding(start = 18.dp, top = 15.dp)
                .background(
                    color = KieroTheme.colors.main,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(vertical = 2.dp, horizontal = 10.dp)
                .align(Alignment.TopStart)
        )
    }
}

@Composable
private fun NextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    color: Color
) {
    Row(
        modifier = modifier
            .noRippleClickable { onClick() }
            .padding(vertical = 11.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = KieroTheme.typography.regular.body5,
            color = color
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun SpeechFieldPreview() {
    KidSpeechField(
        name = "주완",
        isVisibleButton = true,

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