package com.kiero.presentation.kid.wish.state

import androidx.compose.runtime.Immutable
import com.kiero.core.model.UiState
import com.kiero.presentation.kid.wish.model.KidCoinUiModel
import com.kiero.presentation.kid.wish.model.KidWishUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class KidWishState(
    val coinUiModel: KidCoinUiModel = KidCoinUiModel(),
    val kidWishList: UiState<ImmutableList<KidWishUiModel>> = UiState.Success(FAKE)
) {
    val kidName: String
        get() = "${coinUiModel.lastName}${coinUiModel.firstName}"

    companion object {
        val FAKE = persistentListOf(
            KidWishUiModel(
                couponId = 1,
                name = "미션 제목",
                price = 100
            ),
            KidWishUiModel(
                couponId = 2,
                name = "미션 제목",
                price = 100
            ),
            KidWishUiModel(
                couponId = 3,
                name = "미션 제목",
                price = 100
            ),
            KidWishUiModel(
                couponId = 4,
                name = "미션 제목",
                price = 100
            ),
        )
    }
}