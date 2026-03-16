package com.kiero.data.parent.reward.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RewardResponseDto(
    @SerialName("couponId")
    val couponId: Long,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Int,
)