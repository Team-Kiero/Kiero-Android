package com.kiero.domain.parent.mypage

import com.kiero.core.localstorage.TokenManager
import com.kiero.data.parent.mypage.parent.repository.ParentMyPageRepository
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val parentMyPageRepository: ParentMyPageRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(): Result<Unit> {
        val result = parentMyPageRepository.postWithdrawStatus()

        return result.onSuccess {
            tokenManager.clearTokens()
        }
    }
}