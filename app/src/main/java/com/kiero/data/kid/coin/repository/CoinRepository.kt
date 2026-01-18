package com.kiero.data.kid.coin.repository

import com.kiero.data.kid.coin.model.CoinModel
import kotlinx.coroutines.flow.StateFlow

interface CoinRepository {
    val myCoin: StateFlow<CoinModel>
    suspend fun getCurrentCoin() : Result<CoinModel>
}