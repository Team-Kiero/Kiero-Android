package com.kiero.data.parent.reward.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.reward.remote.dto.request.RewardCreateRequestDto
import com.kiero.data.parent.reward.remote.dto.request.RewardUpdateRequestDto
import com.kiero.data.parent.reward.remote.dto.response.RewardResponseDto

interface RewardDataSource {

    suspend fun getCoupons(childId: Long): BaseResponse<List<RewardResponseDto>>

    suspend fun createCoupon(
        childId: Long,
        request: RewardCreateRequestDto,
    ): BaseResponse<RewardResponseDto>

    suspend fun updateCoupon(
        couponId: Long,
        request: RewardUpdateRequestDto,
    ): BaseResponse<RewardResponseDto>

    suspend fun deleteCoupon(couponId: Long): BaseResponse<Unit?>
}