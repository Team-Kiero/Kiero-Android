package com.kiero.presentation.parent.screen.journey.model

import com.kiero.data.auth.model.ChildrenModel

data class KidInfo(
    val kidId: String = "",
    val kidName: String = "",
    val currentDate: String = ""
)

fun ChildrenModel.toUiModel() = KidInfo(
    kidId = childId.toString(),
    kidName = childFirstName
)
