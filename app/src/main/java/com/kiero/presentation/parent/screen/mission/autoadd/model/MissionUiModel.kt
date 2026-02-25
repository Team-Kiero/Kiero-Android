package com.kiero.presentation.parent.screen.mission.autoadd.model

import java.time.LocalDate

data class MissionUiModel(
    val id: Long? = null,
    val name: String,
    val reward: Int,
    val dueAt: LocalDate,
    val isCompleted: Boolean = false
)