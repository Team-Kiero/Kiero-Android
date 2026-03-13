package com.kiero.data.parent.reward.remote.api

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.parent.reward.remote.dto.request.RewardCreateRequestDto
import com.kiero.data.parent.reward.remote.dto.request.RewardUpdateRequestDto
import com.kiero.data.parent.reward.remote.dto.response.RewardResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RewardService {

    @GET("api/v1/coupons/{childId}")
    suspend fun getCoupons(
        @Path("childId") childId: Long,
    ): BaseResponse<List<RewardResponseDto>>

    @POST("api/v1/coupons/{childId}")
    suspend fun createCoupon(
        @Path("childId") childId: Long,
        @Body request: RewardCreateRequestDto,
    ): BaseResponse<RewardResponseDto>

    @PATCH("api/v1/coupons/{couponId}")
    suspend fun updateCoupon(
        @Path("couponId") couponId: Long,
        @Body request: RewardUpdateRequestDto,
    ): BaseResponse<RewardResponseDto>

    @DELETE("api/v1/coupons/{couponId}")
    suspend fun deleteCoupon(
        @Path("couponId") couponId: Long,
    ): BaseResponse<Unit?>
}