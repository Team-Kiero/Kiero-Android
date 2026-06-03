package com.kiero.data.kid.mypage.model

import com.kiero.data.kid.mypage.remote.dto.response.KidTermResponseDto

data class KidTermsModel(
    val linkType: String,
    val link: String
)

fun KidTermResponseDto.toModel() = KidTermsModel(
    linkType = linkType,
    link = link
)
