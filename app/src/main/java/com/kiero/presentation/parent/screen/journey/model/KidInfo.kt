package com.kiero.presentation.parent.screen.journey.model

import com.kiero.core.common.util.toDotSeparatedDate
import com.kiero.data.auth.model.ChildrenModel
import java.time.LocalDate

data class KidInfo(
    val kidId: String = "",
    val kidName: String = "",
    val currentDate: String = LocalDate.now().toDotSeparatedDate()
)

fun ChildrenModel.toUiModel() = KidInfo(
    kidId = childId.toString(),
    kidName = childFirstName
)
