package com.kiero.data.kid.user.model

import com.kiero.data.kid.user.remote.dto.response.KidParentWithdrawalStatusResponseDto

data class KidParentWithdrawalStatusModel(
    val isParentWithdrawn: Boolean,
)

fun KidParentWithdrawalStatusResponseDto.toModel() = KidParentWithdrawalStatusModel(
    isParentWithdrawn = isParentWithdrawn,
)
