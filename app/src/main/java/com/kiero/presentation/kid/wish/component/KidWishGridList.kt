package com.kiero.presentation.kid.wish.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.kid.wish.model.KidWishUiModel
import com.kiero.presentation.kid.wish.state.KidWishState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun KidWishGridList(
    wishList: ImmutableList<KidWishUiModel>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        wishList.chunked(2).forEach { columnItems ->

            Column(
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                KidWishGridItem(
                    reward = columnItems[0].price,
                    missionTitle = columnItems[0].name,
                    onClickWish = {}
                )

                if (columnItems.size > 1) {
                    KidWishGridItem(
                        reward = columnItems[1].price,
                        missionTitle = columnItems[1].name,
                        onClickWish = {}
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun KidWishGridListPreview() {
    KieroTheme {
        KidWishGridList(
            wishList = KidWishState.FAKE
        )
    }
}