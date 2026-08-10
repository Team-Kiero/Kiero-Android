package com.kiero.data.kid.coin.model

import com.kiero.data.kid.coin.remote.dto.response.CoinResponseDto

data class CoinModel(
    val id: Int = 0,
    val lastName: String = "",
    val firstName: String = "",
    val coinAmount: Int = 0,
    val today: String = ""
)

fun CoinResponseDto.toModel() = CoinModel(
    id = this.id,
    lastName = this.lastName,
    firstName = this.firstName,
    coinAmount = this.coinAmount,
    today = this.today
)
