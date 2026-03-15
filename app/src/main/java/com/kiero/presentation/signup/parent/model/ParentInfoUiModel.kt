package com.kiero.presentation.signup.parent.model

import androidx.compose.runtime.Immutable
import com.kiero.core.model.parent.ParentInfo

@Immutable
data class ParentInfoUiModel(
    val parentName: String = "",
    val parentProfileImage: String = "",
)

fun ParentInfo.toUiModel() = ParentInfoUiModel(
    parentName = name,
    parentProfileImage = profileImage,
)


