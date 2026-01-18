package com.kiero.data.coin.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.coin.remote.dto.response.CoinResponseDto
import retrofit2.http.GET

interface CoinService {
    @GET("api/v1/children/me")
    suspend fun getCurrentCoin(

    ) : BaseResponse<CoinResponseDto>
}