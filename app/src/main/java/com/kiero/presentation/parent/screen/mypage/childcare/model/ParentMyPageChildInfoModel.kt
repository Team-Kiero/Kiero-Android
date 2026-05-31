package com.kiero.presentation.parent.screen.mypage.childcare.model

data class ParentMyPageChildInfoModel(
    val code: String = "",
    val childLastName: String = "",
    val childFirstName: String = "",
    val isChildJoined: Boolean = false
) {
    val fullName: String = "$childLastName$childFirstName"
}
