package com.kiero.data.parent.mypage.kid.model

import com.kiero.data.parent.mypage.kid.remote.dto.response.KidTermResponseDto

data class KidTermsModel(
    val linkType: String,
    val link: String
)

fun KidTermResponseDto.toModel() = KidTermsModel(
    linkType = linkType,
    link = link
)
