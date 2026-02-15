package com.kiero.domain.repository.kid.wish

import com.kiero.domain.entity.kid.wish.WishModel

interface WishRepository {
    suspend fun getCoupons(): Result<List<WishModel>>

    suspend fun patchCoupon(couponId: Long): Result<WishModel>
}