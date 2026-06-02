package com.kiero.domain.kid.user.usecase

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.auth.UserRole
import com.kiero.data.kid.user.repository.KidUserRepository
import javax.inject.Inject

class CheckParentStatusUseCase @Inject constructor(
    private val userInfoManager: UserInfoManager,
    private val kidUserRepository: KidUserRepository
) {
    suspend operator fun invoke(): Result<Boolean> = suspendRunCatching {
        val userRole = userInfoManager.getUserRole()

        if (userRole != UserRole.KID) {
            return@suspendRunCatching false
        }

        val statusModel = kidUserRepository.getParentWithdrawalStatus().getOrThrow()
        statusModel.isParentWithdrawn
    }
}