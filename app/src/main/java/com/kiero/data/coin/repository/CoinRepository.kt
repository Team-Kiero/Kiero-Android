package com.kiero.data.coin.repository

import com.kiero.data.coin.model.CoinModel
import kotlinx.coroutines.flow.StateFlow

interface CoinRepository {
    val myCoin: StateFlow<CoinModel>
    suspend fun getCurrentCoin() : Result<CoinModel>
}