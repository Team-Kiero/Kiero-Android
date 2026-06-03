package com.kiero.data.kid.user.repository

import com.kiero.data.kid.user.model.KidParentWithdrawalStatusModel

interface KidUserRepository {
    suspend fun getParentWithdrawalStatus(): Result<KidParentWithdrawalStatusModel>
}