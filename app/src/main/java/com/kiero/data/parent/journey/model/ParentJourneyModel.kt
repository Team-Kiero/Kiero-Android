package com.kiero.data.parent.journey.model

import com.kiero.data.parent.journey.remote.dto.response.ParentJourneyMissionDto
import com.kiero.data.parent.journey.remote.dto.response.ParentJourneyResponseDto
import com.kiero.data.parent.journey.remote.dto.response.ParentJourneyScheduleDto

data class ParentJourneyModel(
    val isFireLitToday : Boolean,
    val completeMissions : List<ParentJourneyMissionModel>,
    val incompleteMissions : List<ParentJourneyMissionModel>,
    val schedules : List<ParentJourneyScheduleModel>
)

data class ParentJourneyMissionModel(
    val name : String,
    val reward : Int
)

data class ParentJourneyScheduleModel(
    val name : String,
    val startTime : String,
    val endTime : String,
    val isOngoing : Boolean,
    val imageUrl : String?,
    val status : String
)

fun ParentJourneyResponseDto.toModel() = ParentJourneyModel(
    isFireLitToday = isFireLitToday,
    completeMissions = completeMissions.map { it.toModel() },
    incompleteMissions = incompleteMissions.map { it.toModel() },
    schedules = schedules.map { it.toModel() }
)

fun ParentJourneyMissionDto.toModel() = ParentJourneyMissionModel(
    name = name,
    reward = reward
)

fun ParentJourneyScheduleDto.toModel() = ParentJourneyScheduleModel(
    name = name,
    startTime = startTime,
    endTime = endTime,
    isOngoing = isOngoing,
    imageUrl = imageUrl,
    status = status
)
