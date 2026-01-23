package com.kiero.presentation.kid.wish.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
    onClickWish: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        wishList.chunked(2).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                KidWishGridItem(
                    reward = rowItems[0].price,
                    missionTitle = rowItems[0].name,
                    onClickWish = {
                        onClickWish(rowItems[0].couponId)
                    },
                    modifier = Modifier.weight(1f)
                )

                if (rowItems.size > 1) {
                    KidWishGridItem(
                        reward = rowItems[1].price,
                        missionTitle = rowItems[1].name,
                        onClickWish = {
                            onClickWish(rowItems[1].couponId)
                        },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
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
            wishList = KidWishState.FAKE,
            onClickWish = {}
        )
    }
}