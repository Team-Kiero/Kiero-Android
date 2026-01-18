package com.kiero.data.kid.wish.model

import com.kiero.data.kid.wish.remote.dto.response.WishResponseDto

data class WishModel(
    val id: Long,
    val name: String,
    val price: Int,
)

fun WishResponseDto.toModel() = WishModel(
    id = id,
    name = name,
    price = price
)
