package com.kiero.domain.parent.invite.usecase

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.parent.signup.model.ParentSignUpModel
import com.kiero.data.parent.signup.repository.ParentSignUpRepository
import javax.inject.Inject

class GetInviteCode @Inject constructor(
    private val parentSignUpRepository: ParentSignUpRepository
) {
    suspend operator fun invoke(
        childLastName: String,
        childFirstName: String
    ): Result<ParentSignUpModel> = suspendRunCatching {
        return parentSignUpRepository.postSignUp(
            childLastName = childLastName,
            childFirstName = childFirstName
        )
    }
}
