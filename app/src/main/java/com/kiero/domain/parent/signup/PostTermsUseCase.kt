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
        val termsIds = userInfoManager.getAgreedTermsIds() ?: emptyList()
        if (termsIds.isEmpty()) {
            throw IllegalStateException("약관 동의가 필요합니다.")
        }

        termsRepository.postTermsStatus(
            request = TermsAgreementModel(
                termsIds = termsIds
            )
        ).getOrThrow()

        userInfoManager.saveTermsInfo(isRequiredTermsAllAgreed = true)
    }
}
