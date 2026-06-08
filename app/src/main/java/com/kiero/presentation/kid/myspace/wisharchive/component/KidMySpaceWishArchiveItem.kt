package com.kiero.presentation.kid.myspace.wisharchive.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.R
import com.kiero.core.designsystem.component.button.KieroButtonSmall
import com.kiero.core.designsystem.component.chip.KieroChip
import com.kiero.core.designsystem.component.chip.action.KieroCoinAction
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun KidMySpaceWishArchiveItem(
    hasWish: Boolean,
    isToday: Boolean,
    modifier: Modifier = Modifier,
    title: String = "",
    date: String = "",
    price: Int = 0,
    onActionClick: () -> Unit = {}
) {
    val displayTitle = if (hasWish) title else "아직 빌었던 소원이 없어!"
    val displaySubtitle = if (hasWish) "획득일 | $date" else "오늘의 소원을 빌어볼까?"


    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KieroTheme.colors.gray900,
                shape = RoundedCornerShape(10.dp)
            )
            .then(
                if (isToday || !hasWish) Modifier.border(
                    width = 1.dp,
                    color = KieroTheme.colors.gray300,
                    shape = RoundedCornerShape(10.dp)
                ) else Modifier
            )
            .padding(horizontal = 11.dp, vertical = 16.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_kid_well_of_wish),
                contentDescription = null,
                tint = KieroTheme.colors.main,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(18.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = displayTitle,
                    style = KieroTheme.typography.semiBold.title3,
                    color = KieroTheme.colors.white
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = displaySubtitle,
                    style = KieroTheme.typography.regular.body4,
                    color = KieroTheme.colors.gray400
                )
            }

            if (hasWish) {
                KieroChip(
                    isCompleted = false,
                    isEnabled = false,
                    action = KieroCoinAction(
                        coinCount = price,
                        isCompleted = false,
                        isEnabled = false,
                        isUseMode = true,
                        viewType = null,
                        onClick = {}
                    ),
                    backgroundColor = KieroTheme.colors.gray800,
                    hasBorder = false
                )
            }
        }

        if (!hasWish) {
            Spacer(modifier = Modifier.height(10.dp))

            KieroButtonSmall(
                text = "소원 빌러가기",
                onClick = onActionClick,
                containerColor = KieroTheme.colors.white,
                contentColor = KieroTheme.colors.black,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun KidMySpaceWishArchiveItemPreview() {
    KieroTheme {
        Column (
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            KidMySpaceWishArchiveItem(
                hasWish = true,
                isToday = true,
                title = "키어로 스프린트하기!!",
                date = "5월 10일",
                price = 500
            )
            KidMySpaceWishArchiveItem(
                hasWish = true,
                isToday = false,
                title = "키어로 스프린트하기!!",
                date = "5월 10일",
                price = 100
            )

            KidMySpaceWishArchiveItem(
                hasWish = false,
                isToday = false
            )
        }
    }
}