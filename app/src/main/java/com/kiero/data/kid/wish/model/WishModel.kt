package com.kiero.data.kid.wish.model

import com.kiero.data.kid.wish.remote.dto.response.WishResponseDto

data class WishModel(
    val couponId: Long,
    val name: String,
    val price: Int,
)

fun WishResponseDto.toModel() = WishModel(
    couponId = couponId,
    name = name,
    price = price
)
