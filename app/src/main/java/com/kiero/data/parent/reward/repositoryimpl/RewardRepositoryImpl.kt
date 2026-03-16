package com.kiero.data.parent.reward.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.reward.model.RewardModel
import com.kiero.data.parent.reward.model.toModel
import com.kiero.data.parent.reward.remote.datasource.RewardDataSource
import com.kiero.data.parent.reward.remote.dto.request.RewardCreateRequestDto
import com.kiero.data.parent.reward.remote.dto.request.RewardUpdateRequestDto
import com.kiero.data.parent.reward.repository.RewardRepository
import javax.inject.Inject

class RewardRepositoryImpl @Inject constructor(
    private val dataSource: RewardDataSource,
) : RewardRepository {

    override suspend fun getCoupons(childId: Long): Result<List<RewardModel>> =
        suspendRunCatching {
            dataSource.getCoupons(childId).data!!.map { it.toModel() }
        }

    override suspend fun createCoupon(
        childId: Long,
        name: String,
        price: Int,
    ): Result<RewardModel> = suspendRunCatching {
        val requestDto = RewardCreateRequestDto(name = name, price = price)
        dataSource.createCoupon(childId, requestDto).data!!.toModel()
    }

    override suspend fun updateCoupon(
        couponId: Long,
        name: String,
        price: Int,
    ): Result<RewardModel> = suspendRunCatching {
        val requestDto = RewardUpdateRequestDto(name = name, price = price)
        dataSource.updateCoupon(couponId, requestDto).data!!.toModel()
    }

    override suspend fun deleteCoupon(couponId: Long): Result<Unit> =
        suspendRunCatching {
            dataSource.deleteCoupon(couponId)
            Unit
        }
}