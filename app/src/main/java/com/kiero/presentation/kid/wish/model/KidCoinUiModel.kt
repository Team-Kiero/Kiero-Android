package com.kiero.presentation.kid.wish.model

import androidx.compose.runtime.Immutable
import com.kiero.data.kid.coin.model.CoinModel

@Immutable
data class KidCoinUiModel(
    val lastName : String = "",
    val firstName : String = "",
    val coinAmount : Int = 0,
)

fun CoinModel.toState() = KidCoinUiModel(
    lastName = lastName,
    firstName = firstName,
    coinAmount = coinAmount
)
