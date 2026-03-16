package com.kiero.data.parent.reward.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RewardCreateRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: Int,
)