package com.kiero.domain.login

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.auth.parent.model.KakaoLoginResult
import javax.inject.Inject

class HandleKakaoLoginResultUseCase @Inject constructor(
    private val authRepository: AuthRepository,
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

        val children = authRepository.getChildren().getOrThrow()

        if (children.isNotEmpty()) {
            userInfoManager.saveChildIdInfo(childId = children.first().childId)
            KakaoLoginResult.HasChildren(firstChildId = children.first().childId.toInt())
        } else {
            KakaoLoginResult.NoChildren(
                parentName = name,
                parentProfileImage = image
            )
        }
    }
}
