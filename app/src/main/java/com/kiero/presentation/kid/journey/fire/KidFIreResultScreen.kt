package com.kiero.presentation.kid.journey.fire

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.KieroGifImage
import com.kiero.core.designsystem.component.KieroToolTip
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroTextAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.component.KidSpeechField
import com.kiero.presentation.kid.journey.fire.component.StoneMoving

@Composable
fun KidFireResultRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    KidFIreResultScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
    )
}

@Composable
private fun KidFIreResultScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    isFinished: Boolean = false
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageWidth = screenWidth + 108.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Todo: 오른쪽 아이콘 제거 필요
            KieroTopbar(
                title = "12월 5일 목요일",
                leftIconRes = R.drawable.ic_arrow_left,
                rightIconRes = R.drawable.ic_arrow_right,
                leftIconClick = {},
                rightIconClick = {},
                modifier = Modifier
                    .padding(top = 10.dp)
                    .alpha(if (!isFinished) 1f else 0f),
                textColor = KieroTheme.colors.gray500,
                textStyle = KieroTheme.typography.regular.body3
            )


            Image(
                painter = painterResource(id = R.drawable.img_kid_journey_mask_background),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .wrapContentSize(
                        align = Alignment.Center,
                        unbounded = true
                    )
                    .requiredWidth(imageWidth),
                contentScale = ContentScale.FillWidth
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KieroToolTip(
                message = "불조각을 나에게 건네줘!",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 183.dp)
                    .alpha(if (!isFinished) 1f else 0f),
            )

            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
            ) {
                if (isFinished) {
                    KieroGifImage(
                        drawableId = R.drawable.gif_kid_fire,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 200.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_kid_goblin),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                    )

                    KieroChip(
                        action = KieroTextAction(
                            text = "꾸비",
                            onClick = {}
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 9.dp),
                        horizontalPadding = 20,
                        verticalPadding = 4,
                    )
                }
            }
        }

        if (!isFinished) {
            StoneMoving(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 240.dp)
            )
        }

        if (isFinished) {
            KidSpeechField(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 107.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("덕분에 ")
                        withStyle(style = SpanStyle(color = KieroTheme.colors.main)) {
                            append("영혼의 불꽃")
                        }
                        append(" 이 커졌어")
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
    }
}

@Composable
@Preview
private fun KidFIreScreenPreview() {
    KieroTheme {
        KidFIreResultScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
            isFinished = true
        )
    }
}