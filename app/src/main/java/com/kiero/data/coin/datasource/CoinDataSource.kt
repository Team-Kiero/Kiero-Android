package com.kiero.data.coin.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.coin.dto.response.CoinResponseDto

interface CoinDataSource {
    suspend fun getCurrentCoin() : BaseResponse<CoinResponseDto>
}
