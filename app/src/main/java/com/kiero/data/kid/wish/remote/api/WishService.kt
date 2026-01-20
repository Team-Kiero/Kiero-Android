package com.kiero.data.kid.wish.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.wish.remote.dto.response.WishResponseDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface WishService {
    @GET("api/v1/coupons")
    suspend fun getCoupons() : BaseResponse<List<WishResponseDto>>

    @PATCH("api/v1/coupons/{couponId}")
    suspend fun patchCoupon(
        @Path("couponId") couponId: Long,
    ) : BaseResponse<WishResponseDto>
}