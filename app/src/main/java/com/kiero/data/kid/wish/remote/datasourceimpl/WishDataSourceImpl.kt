package com.kiero.data.kid.wish.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.wish.remote.api.WishService
import com.kiero.data.kid.wish.remote.datasource.WishDataSource
import com.kiero.data.kid.wish.remote.dto.response.WishResponseDto
import javax.inject.Inject

class WishDataSourceImpl @Inject constructor(
    private val wishService: WishService
) : WishDataSource {
    override suspend fun getCoupons(): BaseResponse<List<WishResponseDto>> =
        wishService.getCoupons()

    override suspend fun patchCoupon(couponId: Long): BaseResponse<WishResponseDto> =
        wishService.patchCoupon(couponId)
}