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
            // 앱 재설치 등으로 로컬 데이터가 없는 경우, 서버에서 동의 여부 재확인
            val isAlreadyAgreed = termsRepository.getTermsStatus()
                .getOrNull()?.isRequiredTermsAllAgreed == true

            if (isAlreadyAgreed) {
                userInfoManager.saveTermsInfo(isRequiredTermsAllAgreed = true)
                return@suspendRunCatching
            }

            throw IllegalStateException("약관 동의 정보를 찾을 수 없습니다. 다시 로그인해 주세요.")
        }

        termsRepository.postTermsStatus(
            request = TermsAgreementModel(
                termsIds = termsIds
            )
        ).getOrThrow()

        userInfoManager.saveTermsInfo(isRequiredTermsAllAgreed = true)
    }
}
