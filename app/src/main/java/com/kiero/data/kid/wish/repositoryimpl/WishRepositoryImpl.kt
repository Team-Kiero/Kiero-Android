package com.kiero.data.kid.wish.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.kid.wish.model.WishHistoryModel
import com.kiero.data.kid.wish.model.WishModel
import com.kiero.data.kid.wish.model.toModel
import com.kiero.data.kid.wish.remote.datasource.WishDataSource
import com.kiero.data.kid.wish.repository.WishRepository
import javax.inject.Inject

class WishRepositoryImpl @Inject constructor(
    private val wishDataSource: WishDataSource,
) : WishRepository {
    override suspend fun getCoupons(): Result<List<WishModel>> = suspendRunCatching {
        wishDataSource.getCoupons().data!!.map { it.toModel() }
    }

    override suspend fun patchCoupon(couponId: Long): Result<WishModel> = suspendRunCatching {
        wishDataSource.patchCoupon(couponId = couponId).data!!.toModel()
    }

    override suspend fun getWishHistory(): Result<List<WishHistoryModel>> = suspendRunCatching {
        wishDataSource.getWishHistory().data!!.map { it.toModel() }
    }
}