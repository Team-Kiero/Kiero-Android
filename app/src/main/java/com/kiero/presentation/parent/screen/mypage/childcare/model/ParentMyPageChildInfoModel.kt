package com.kiero.presentation.parent.screen.mypage.childcare.model

import androidx.compose.foundation.text.input.TextFieldState

data class ParentMyPageChildInfoModel(
    val code: String = "",
    val childLastName: TextFieldState = TextFieldState(),
    val childFirstName: TextFieldState = TextFieldState()
)