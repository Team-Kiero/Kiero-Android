package com.kiero.data.kid.wish.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.wish.remote.dto.response.WishHistoryResponseDto
import com.kiero.data.kid.wish.remote.dto.response.WishResponseDto

interface WishDataSource {
    suspend fun getCoupons(): BaseResponse<List<WishResponseDto>>
    suspend fun patchCoupon(couponId: Long): BaseResponse<WishResponseDto>
    suspend fun getWishHistory(): BaseResponse<List<WishHistoryResponseDto>>
}