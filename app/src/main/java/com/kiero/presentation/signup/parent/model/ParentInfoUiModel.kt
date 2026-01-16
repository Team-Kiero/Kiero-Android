package com.kiero.presentation.signup.parent.model

import androidx.compose.runtime.Immutable

@Immutable
data class ParentInfoUiModel(
    val parentName: String = "",
    val parentProfileImage: String = "",
) {
    val formattedParentName: String
        get() = parentName.drop(1) + "맘"
}
