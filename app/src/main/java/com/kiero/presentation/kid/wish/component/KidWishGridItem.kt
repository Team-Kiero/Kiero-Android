package com.kiero.presentation.kid.wish.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.common.extension.noRippleClickable
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidWishGridItem(
    reward: Int,
    missionTitle: String,
    onClickWish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 13.dp, vertical = 12.dp)
    ) {
        Text(
            text = "금화 $reward 개",
            style = KieroTheme.typography.regular.body4,
            color = KieroTheme.colors.gray400
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = missionTitle,
            style = KieroTheme.typography.regular.body3,
            color = KieroTheme.colors.white
        )

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = KieroTheme.colors.white,
                    shape = RoundedCornerShape(8.dp)
                )
                .noRippleClickable(onClick = onClickWish)
                .padding(horizontal = 40.dp, vertical = 10.dp)
        ) {
            Text(
                text = "소원빌기",
                style = KieroTheme.typography.semiBold.title4,
                color = KieroTheme.colors.black
            )
        }
    }
}

@Preview
@Composable
private fun KidWishGridItemPreview() {
    KieroTheme {
        KidWishGridItem(
            reward = 100,
            missionTitle = "미션 제목",
            onClickWish = {}
        )
    }
}