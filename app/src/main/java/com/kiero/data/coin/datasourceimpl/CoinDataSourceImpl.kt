package com.kiero.data.coin.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.coin.api.CoinService
import com.kiero.data.coin.datasource.CoinDataSource
import com.kiero.data.coin.dto.response.CoinResponseDto
import javax.inject.Inject

class CoinDataSourceImpl @Inject constructor(
    private val service: CoinService
) : CoinDataSource {
    override suspend fun getCurrentCoin(): BaseResponse<CoinResponseDto> =
        service.getCurrentCoin()
}