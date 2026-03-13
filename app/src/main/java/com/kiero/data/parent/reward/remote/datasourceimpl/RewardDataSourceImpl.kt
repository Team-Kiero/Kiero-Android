package com.kiero.data.parent.reward.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.reward.remote.api.RewardService
import com.kiero.data.parent.reward.remote.datasource.RewardDataSource
import com.kiero.data.parent.reward.remote.dto.request.RewardCreateRequestDto
import com.kiero.data.parent.reward.remote.dto.request.RewardUpdateRequestDto
import com.kiero.data.parent.reward.remote.dto.response.RewardResponseDto
import javax.inject.Inject

class RewardDataSourceImpl @Inject constructor(
    private val rewardService: RewardService,
) : RewardDataSource {

    override suspend fun getCoupons(childId: Long): BaseResponse<List<RewardResponseDto>> =
        rewardService.getCoupons(childId)

    override suspend fun createCoupon(
        childId: Long,
        request: RewardCreateRequestDto,
    ): BaseResponse<RewardResponseDto> =
        rewardService.createCoupon(childId, request)

    override suspend fun updateCoupon(
        couponId: Long,
        request: RewardUpdateRequestDto,
    ): BaseResponse<RewardResponseDto> =
        rewardService.updateCoupon(couponId, request)

    override suspend fun deleteCoupon(couponId: Long): BaseResponse<Unit?> =
        rewardService.deleteCoupon(couponId)
}