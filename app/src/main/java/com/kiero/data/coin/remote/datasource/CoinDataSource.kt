package com.kiero.data.coin.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.coin.remote.dto.response.CoinResponseDto

interface CoinDataSource {
    suspend fun getCurrentCoin() : BaseResponse<CoinResponseDto>
}
