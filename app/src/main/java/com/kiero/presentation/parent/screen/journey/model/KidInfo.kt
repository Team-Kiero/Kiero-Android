package com.kiero.presentation.parent.screen.journey.model

import com.kiero.data.auth.model.ChildrenModel
import java.time.LocalDate

data class KidInfo(
    val kidId: String = "",
    val kidName: String = "",
    val currentDate: LocalDate = LocalDate.now()
)

fun ChildrenModel.toUiModel() = KidInfo(
    kidId = childId.toString(),
    kidName = childFirstName
)
