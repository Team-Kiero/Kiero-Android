package com.kiero.data.parent.reward.model

import com.kiero.data.parent.reward.remote.dto.response.RewardResponseDto

data class RewardModel(
    val couponId: Long,
    val name: String,
    val price: Int,
)

fun RewardResponseDto.toModel() = RewardModel(
    couponId = this.couponId,
    name = this.name,
    price = this.price,
)