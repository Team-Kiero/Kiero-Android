package com.kiero.core.designsystem.component.emptyview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

/** 일러스트가 들어간 전체 화면용 빈 화면*/
@Composable
fun KieroEntireEmptyScreen(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int = R.drawable.img_parent_no_alarm,
    contentScale: ContentScale= ContentScale.FillWidth,
    text: String = "등록된 일정이 없어요.\n우측 하단 버튼을 눌러 일정을 추가해보세요!",
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.gray400,
            textAlign = TextAlign.Center,
        )

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(154f / 186f),
            contentScale = contentScale,
        )
    }
}

@Preview
@Composable
private fun KieroEntireEmptyScreenPreview() {
    KieroTheme {
        KieroEntireEmptyScreen()
    }
}
