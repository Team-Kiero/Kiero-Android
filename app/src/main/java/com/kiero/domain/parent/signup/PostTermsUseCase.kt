package com.kiero.domain.parent.signup

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.terms.model.TermsAgreementModel
import com.kiero.data.terms.repository.TermsRepository
import javax.inject.Inject

class PostTermsUseCase @Inject constructor(
    private val termsRepository: TermsRepository,
    private val userInfoManager: UserInfoManager
) {
    suspend operator fun invoke() : Result<Unit> = suspendRunCatching{
        val isTermsAgreed = userInfoManager.getTermsInfo() ?: false
        if (!isTermsAgreed) {
            throw IllegalStateException("약관 동의가 필요합니다.")
        }

        val termsIds = userInfoManager.getAgreedTermsIds() ?: emptyList()

        termsRepository.postTermsStatus(
            request = TermsAgreementModel(
                termsIds = termsIds
            )
        )
    }
}