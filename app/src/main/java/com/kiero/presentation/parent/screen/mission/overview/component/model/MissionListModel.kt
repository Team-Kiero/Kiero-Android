package com.kiero.presentation.parent.screen.mission.overview.component.model

import androidx.compose.runtime.Immutable
import com.kiero.data.parent.mission.model.MissionByDateModel
import com.kiero.data.parent.mission.model.MissionListModel
import com.kiero.data.parent.mission.model.MissionModel
import com.kiero.presentation.kid.mission.util.createDateTitle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class ParentMissionByDateUiModel(
    val missionsByDate: ImmutableList<ParentMissionListUiModel> = persistentListOf(),
)

@Immutable
data class ParentMissionListUiModel(
    val dueAt: String,
    val dayOfWeek: String,
    val title: String,
    val missions: ImmutableList<ParentMissionUiModel>,
)

@Immutable
data class ParentMissionUiModel(
    val id: Long,
    val isCompleted: Boolean,
    val name: String,
    val reward: Int,
)

fun MissionByDateModel.toUiModel() = ParentMissionByDateUiModel(
    missionsByDate = this.missionsByDate.map { it.toUiModel() }.toImmutableList()
)

fun MissionListModel.toUiModel() = ParentMissionListUiModel(
    dueAt = this.dueAt,
    dayOfWeek = this.dayOfWeek,
    missions = this.missions.map { it.toUiModel() }.toImmutableList(),
    title = createDateTitle(this.dueAt, this.dayOfWeek),
)

fun MissionModel.toUiModel() = ParentMissionUiModel(
    id = this.id,
    isCompleted = this.isCompleted,
    name = this.name,
    reward = this.reward
)