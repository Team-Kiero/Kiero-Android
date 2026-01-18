package com.kiero.data.kid.coin.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinResponseDto(
    @SerialName("lastName")
    val lastName: String,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("coinAmount")
    val coinAmount: Int
)