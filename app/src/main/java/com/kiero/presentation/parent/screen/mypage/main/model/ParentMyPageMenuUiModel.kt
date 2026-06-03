package com.kiero.presentation.parent.screen.mypage.main.model

import com.kiero.data.parent.mypage.model.ParentTermsModel

data class ParentMyPageMenuUiModel(
    val linkType: ParentMenuLinkType,
    val link: String
)

fun ParentTermsModel.toUiModel() = ParentMyPageMenuUiModel(
    linkType = ParentMenuLinkType.from(this.linkType),
    link = this.link
)