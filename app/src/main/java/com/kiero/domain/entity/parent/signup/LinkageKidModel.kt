package com.kiero.domain.entity.parent.signup

import com.kiero.data.parent.signup.remote.dto.response.LinkageKidResponseDto

data class LinkageKidModel(
    val isRegistered: Boolean,
    val childId: Long
)

fun LinkageKidResponseDto.toModel() = LinkageKidModel(
    isRegistered = isRegistered,
    childId = childId
)