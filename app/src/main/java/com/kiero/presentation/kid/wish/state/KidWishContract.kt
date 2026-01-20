package com.kiero.presentation.kid.wish.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.model.KidCoinUiModel
import com.kiero.presentation.kid.wish.model.KidWishUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class KidWishState(
    val isVisibleDialog: Boolean = false,
    val isCompletedWish: Boolean = false,
    val isRefreshing: Boolean = false,
    val selectedWishItem: KidWishUiModel? = null,
    val coinUiModel: KidCoinUiModel = KidCoinUiModel(),
    val kidWishList: ImmutableList<KidWishUiModel> = persistentListOf(),
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

sealed interface KidWishSideEffect {
    data class ShowSnackBar(val message: String) : KidWishSideEffect
}