package com.kiero.data.parent.mypage.model

import com.kiero.data.parent.mypage.remote.dto.response.parent.ParentTermResponseDto

data class ParentTermsModel(
    val linkType: String,
    val link: String
)

fun ParentTermResponseDto.toModel() = ParentTermsModel(
    linkType = linkType,
    link = link
)
