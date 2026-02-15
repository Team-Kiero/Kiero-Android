package com.kiero.presentation.kid.wish.model

import androidx.compose.runtime.Immutable
import com.kiero.data.kid.wish.model.WishModel

@Immutable
data class KidWishUiModel(
    val couponId: Long,
    val name: String,
    val price: Int,
)

fun WishModel.toUiModel() = KidWishUiModel(
    couponId = couponId,
    name = name,
    price = price
)
