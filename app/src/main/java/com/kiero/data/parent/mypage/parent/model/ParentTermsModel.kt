package com.kiero.data.parent.mypage.parent.model

import com.kiero.data.parent.mypage.parent.remote.dto.response.parent.ParentTermResponseDto

data class ParentTermsModel(
    val linkType: String,
    val link: String
)

fun ParentTermResponseDto.toModel() = ParentTermsModel(
    linkType = linkType,
    link = link
)
