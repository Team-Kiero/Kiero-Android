package com.kiero.data.kid.wish.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WishResponseDto(
    @SerialName("couponId")
    val couponId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Int,
)