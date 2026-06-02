package com.kiero.presentation.kid.myspace.policy.model

import com.kiero.data.parent.mypage.kid.model.KidTermsModel

data class KidMySpaceMenuUiModel(
    val linkType: KidMenuLinkType,
    val link: String
)

fun KidTermsModel.toUiModel() = KidMySpaceMenuUiModel(
    linkType = KidMenuLinkType.from(this.linkType),
    link = this.link
)