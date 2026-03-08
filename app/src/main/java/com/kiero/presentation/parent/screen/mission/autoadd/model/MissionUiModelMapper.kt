package com.kiero.presentation.parent.screen.mission.autoadd.model

import com.kiero.data.parent.mission.model.MissionSuggestionModel
import com.kiero.data.parent.mission.model.SuggestedMissionModel
import java.time.LocalDate

fun MissionSuggestionModel.toUiModels(): List<MissionUiModel> =
    suggestedMissions.map { it.toUiModel() }

fun SuggestedMissionModel.toUiModel(): MissionUiModel =
    MissionUiModel(
        name = name,
        reward = reward,
        dueAt = LocalDate.parse(dueAt),
        isCompleted = false
    )

fun MissionUiModel.toDomainModel(): SuggestedMissionModel =
    SuggestedMissionModel(
        name = name,
        reward = reward,
        dueAt = dueAt.toString()
    )