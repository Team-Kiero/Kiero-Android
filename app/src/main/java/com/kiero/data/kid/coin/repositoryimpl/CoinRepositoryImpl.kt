package com.kiero.data.kid.coin.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.kid.coin.model.CoinModel
import com.kiero.data.kid.coin.model.toModel
import com.kiero.data.kid.coin.remote.datasource.CoinDataSource
import com.kiero.data.kid.coin.repository.CoinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val dataSource: CoinDataSource
) : CoinRepository {
    private val _myCoin = MutableStateFlow(CoinModel())

    override val myCoin: StateFlow<CoinModel> = _myCoin.asStateFlow()

    override suspend fun getCurrentCoin(): Result<CoinModel> = suspendRunCatching {
        val newCoin = dataSource.getCurrentCoin().data!!.toModel()
        _myCoin.update {
            it.copy(
                lastName = newCoin.lastName,
                firstName = newCoin.firstName,
                coinAmount = newCoin.coinAmount,
                today = newCoin.today
            )
        }

        newCoin
    }
}