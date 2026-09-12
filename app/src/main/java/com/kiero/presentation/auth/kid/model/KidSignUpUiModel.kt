package com.kiero.presentation.auth.kid.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.kiero.core.common.util.InputValidator
import com.kiero.data.auth.model.AuthKidModel

@Stable
data class KidSignUpUiModel(
    val firstName: TextFieldState = TextFieldState(),
    val lastName: TextFieldState = TextFieldState(),
    val inviteCode: TextFieldState = TextFieldState(),
) {
    val validateLastName: Boolean
        get() = InputValidator.isValidName(lastName.text.toString())

    val validateFirstName: Boolean
        get() = InputValidator.isValidName(firstName.text.toString())
}

fun KidSignUpUiModel.toModel() = AuthKidModel(
    firstName = firstName.text.toString(),
    lastName = lastName.text.toString(),
    inviteCode = inviteCode.text.toString()
)

