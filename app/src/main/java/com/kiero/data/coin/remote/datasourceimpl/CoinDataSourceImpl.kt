package com.kiero.data.coin.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.coin.remote.api.CoinService
import com.kiero.data.coin.remote.datasource.CoinDataSource
import com.kiero.data.coin.remote.dto.response.CoinResponseDto
import javax.inject.Inject

class CoinDataSourceImpl @Inject constructor(
    private val service: CoinService
) : CoinDataSource {
    override suspend fun getCurrentCoin(): BaseResponse<CoinResponseDto> =
        service.getCurrentCoin()
}