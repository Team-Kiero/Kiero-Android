package com.kiero.presentation.parent.screen.mypage.childcare.model

data class ParentMyPageChildInfoModel(
    val code: String = "",
    val childLastName: String = "바",
    val childFirstName: String = "바",
    val isChildJoined: Boolean = false
) {
    val fullName: String = "$childLastName$childFirstName"
}
