package com.kiero.presentation.kid.journey.fire

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.common.extension.forcePixelToDp
import com.kiero.core.designsystem.component.KieroToolTip
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroTextAction
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.journey.fire.component.KieroButtonLarge

@Composable
fun KidFireRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    KidFIreScreen(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
    )
}

@Composable
private fun KidFIreScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KieroTheme.colors.black)
            .padding(paddingValues)
    ) {
        // 배경 이미지
        Image(
            painter = painterResource(id = R.drawable.img_kid_journey_mask_background),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 86.dp)
                .fillMaxWidth()
                .forcePixelToDp(painterResource(id = R.drawable.img_kid_journey_mask_background))
        )

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
                    .padding(top = 10.dp),
                textColor = KieroTheme.colors.gray500,
                textStyle = KieroTheme.typography.regular.body3
            )

            KieroToolTip(
                message = "불조각을 나에게 건네줘!",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 84.dp)
            )

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_kid_goblin),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 44.dp)
                )

                KieroChip(
                    action = KieroTextAction(
                        text = "꾸비",
                        onClick = {}
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 1.dp),
                    horizontalPadding = 20,
                    verticalPadding = 4,
                )
            }
            KieroButtonLarge(
                stoneCount = 7,
                onClick = {},
                modifier = Modifier
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
@Preview
private fun KidFIreScreenPreview() {
    KieroTheme {
        KidFIreScreen(
            paddingValues = PaddingValues(),
            navigateUp = {},
        )
    }
}