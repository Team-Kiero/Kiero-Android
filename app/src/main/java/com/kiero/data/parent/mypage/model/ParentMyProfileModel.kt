package com.kiero.data.parent.mypage.model

import com.kiero.data.parent.mypage.remote.dto.response.parent.ParentMyProfileResponseDto

data class ParentMyProfileModel(
    val image: String?,
    val name: String,
    val hasPendingChildSession: Boolean,
    val pushNotificationEnabled: Boolean
)

fun ParentMyProfileResponseDto.toModel() = ParentMyProfileModel(
    image = image,
    name = name,
    hasPendingChildSession = hasPendingChildSession,
    pushNotificationEnabled = pushNotificationEnabled
)
