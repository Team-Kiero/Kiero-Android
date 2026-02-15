package com.kiero.domain.repository.kid.coin

import com.kiero.domain.entity.kid.coin.CoinModel
import kotlinx.coroutines.flow.StateFlow

interface CoinRepository {
    val myCoin: StateFlow<CoinModel>
    suspend fun getCurrentCoin() : Result<CoinModel>
}