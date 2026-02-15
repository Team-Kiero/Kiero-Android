package com.kiero.domain.entity.kid.coin

import com.kiero.data.kid.coin.remote.dto.response.CoinResponseDto

data class CoinModel(
    val lastName: String = "",
    val firstName: String = "",
    val coinAmount: Int = 0,
    val today: String = ""
)

fun CoinResponseDto.toModel() = CoinModel(
    lastName = this.lastName,
    firstName = this.firstName,
    coinAmount = this.coinAmount,
    today = this.today
)
