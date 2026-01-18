package com.kiero.presentation.signup.parent.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.kiero.core.common.util.InputValidator
import com.kiero.data.parent.signup.model.ParentSignUpModel

@Immutable
data class ParentSignUpChildInfoUiModel(
    val code: String = "",
    val childLastName: TextFieldState = TextFieldState(),
    val childFirstName: TextFieldState = TextFieldState()
) {
    val validateLastName: Boolean
        get() = InputValidator.isValidName(childLastName.text.toString())

    val validateFirstName: Boolean
        get() = InputValidator.isValidName(childFirstName.text.toString())
}

fun ParentSignUpModel.toState() = ParentSignUpChildInfoUiModel(
    code = this.code,
    childLastName = TextFieldState(initialText = this.childLastName),
    childFirstName = TextFieldState(initialText = this.childFirstName)
)