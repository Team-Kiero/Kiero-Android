package com.kiero.domain.login

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.terms.repository.TermsRepository
import com.kiero.domain.login.model.KakaoLoginResult
import javax.inject.Inject

class HandleKakaoLoginResultUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val termsRepository: TermsRepository,
    private val userInfoManager: UserInfoManager
) {
    suspend operator fun invoke(
        name: String,
        image: String
    ): Result<KakaoLoginResult> = suspendRunCatching {
        userInfoManager.saveParentInfo(
            parentName = name,
            parentProfileImage = image
        )

        val termsStatus = termsRepository.getTermsStatus().getOrThrow()
        userInfoManager.saveTermsInfo(isRequiredTermsAllAgreed = termsStatus.isRequiredTermsAllAgreed)

        if (!termsStatus.isRequiredTermsAllAgreed) {
            return@suspendRunCatching KakaoLoginResult.NeedTermsAgreement
        }

        val children = authRepository.getChildren().getOrThrow()

        if (children.isNotEmpty()) {
            userInfoManager.saveChildIdInfo(childId = children.first().childId)
            KakaoLoginResult.HasChildren(firstChildId = children.first().childId.toInt())
        } else {
            KakaoLoginResult.NoChildren
        }
    }
}
