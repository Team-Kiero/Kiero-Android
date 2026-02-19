package com.kiero.presentation.kid.mission.model

import androidx.compose.runtime.Immutable
import com.kiero.data.parent.mission.model.MissionByDateModel
import com.kiero.data.parent.mission.model.MissionListModel
import com.kiero.data.parent.mission.model.MissionModel
import com.kiero.presentation.kid.mission.util.createDateTitle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class KidMissionByDateUiModel(
    val missionsByDate: ImmutableList<KidMissionListUiModel> = persistentListOf()
)

@Immutable
data class KidMissionListUiModel(
    val dueAt: String,
    val dayOfWeek: String,
    val title: String,
    val missions: ImmutableList<KidMissionUiModel>
)

@Immutable
data class KidMissionUiModel(
    val id: Long,
    val isCompleted: Boolean,
    val name: String,
    val reward: Int
)

fun MissionByDateModel.toUiModel() = KidMissionByDateUiModel(
    missionsByDate = this.missionsByDate.map { it.toUiModel() }.toImmutableList()
)

fun MissionListModel.toUiModel() = KidMissionListUiModel(
    dueAt = this.dueAt,
    dayOfWeek = this.dayOfWeek,
    missions = this.missions.map { it.toUiModel() }.toImmutableList(),
    title = createDateTitle(this.dueAt, this.dayOfWeek),
)

fun MissionModel.toUiModel() = KidMissionUiModel(
    id = this.id,
    isCompleted = this.isCompleted,
    name = this.name,
    reward = this.reward
)