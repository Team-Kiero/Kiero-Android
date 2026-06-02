package com.kiero.domain.parent.splash.usecase

import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.domain.parent.splash.model.ParentAutoLoginResult
import javax.inject.Inject

class CheckParentAutoLoginUseCase @Inject constructor(
    private val userInfoManager: UserInfoManager,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ParentAutoLoginResult {
        // 권한 확인된게 없으면 로그인 화면(역할 선택 화면)으로
        val isTermsAgreed = userInfoManager.getTermsInfo() ?: false
        if (!isTermsAgreed) {
            return ParentAutoLoginResult.MoveToAuth
        }

        val localChildId = userInfoManager.getChildIdInfo()
        if (localChildId != null && localChildId > 0) { // 로컬에 ID가 있다면 바로 홈으로
            return ParentAutoLoginResult.MoveToParentHome
        }

        val childrenResult = authRepository.getChildren()
        return if (childrenResult.isSuccess) {
            val children = childrenResult.getOrThrow()
            if (children.isNotEmpty()) {
                // 서버에서 확인한 자녀 정보를 로컬에 저장하고 홈으로
                userInfoManager.saveChildIdInfo(children.first().childId)
                userInfoManager.saveChildName(children.first().childLastName, children.first().childFirstName)
                ParentAutoLoginResult.MoveToParentHome
            } else {
                // 약관 동의는 했지만 자녀가 없는 경우 (자녀 등록/온보딩 화면으로)
                ParentAutoLoginResult.MoveToOnboarding
            }
        } else {
            // 통신 실패 등의 에러 상황 (로그인 화면으로 보내서 재시도 유도)
            ParentAutoLoginResult.MoveToAuth
        }
    }
}