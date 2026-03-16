package com.kiero.data.parent.reward.repository

import com.kiero.data.parent.reward.model.RewardModel

interface RewardRepository {

    suspend fun getCoupons(childId: Long): Result<List<RewardModel>>

    suspend fun createCoupon(
        childId: Long,
        name: String,
        price: Int,
    ): Result<RewardModel>

    suspend fun updateCoupon(
        couponId: Long,
        name: String,
        price: Int,
    ): Result<RewardModel>

    suspend fun deleteCoupon(couponId: Long): Result<Unit>
}