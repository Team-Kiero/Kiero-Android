package com.kiero.data.kid.wish.model

import com.kiero.data.kid.wish.remote.dto.response.WishHistoryResponseDto
import com.kiero.data.kid.wish.remote.dto.response.WishResponseDto

data class WishModel(
    val couponId: Long,
    val name: String,
    val price: Int,
)

data class WishHistoryModel(
    val name: String,
    val price: Int,
    val purchasedAt: String
)

fun WishResponseDto.toModel() = WishModel(
    couponId = couponId,
    name = name,
    price = price
)

fun WishHistoryResponseDto.toModel() = WishHistoryModel(
    name = name,
    price = price,
    purchasedAt = purchasedAt
)
