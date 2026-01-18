package com.kiero.data.kid.coin.model

import com.kiero.data.kid.coin.remote.dto.response.CoinResponseDto

data class CoinModel(
    val lastName : String = "",
    val firstName: String = "",
    val coinAmount: Int = 0
)

fun CoinResponseDto.toModel() = CoinModel(
    lastName = this.lastName,
    firstName = this.firstName,
    coinAmount = this.coinAmount
)
