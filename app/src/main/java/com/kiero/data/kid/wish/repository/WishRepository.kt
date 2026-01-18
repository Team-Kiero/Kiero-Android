package com.kiero.data.kid.wish.repository

import com.kiero.data.kid.wish.model.WishModel

interface WishRepository {
    suspend fun getCoupons(): Result<List<WishModel>>

    suspend fun patchCoupon(couponId: Long): Result<WishModel>
}