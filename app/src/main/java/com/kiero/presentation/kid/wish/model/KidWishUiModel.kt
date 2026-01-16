package com.kiero.presentation.kid.wish.model

import androidx.compose.runtime.Immutable

@Immutable
data class KidWishUiModel(
    val couponId: Int,
    val name: String,
    val price: Int,
)
