package com.kiero.presentation.auth.kid.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.kiero.data.auth.model.AuthKidModel

@Immutable
data class KidSignUpUiModel(
    val firstName: TextFieldState = TextFieldState(),
    val lastName: TextFieldState = TextFieldState(),
    val inviteCode: TextFieldState = TextFieldState(),
)

fun KidSignUpUiModel.toModel() = AuthKidModel(
    firstName = firstName.text.toString(),
    lastName = lastName.text.toString(),
    inviteCode = inviteCode.text.toString()
)

