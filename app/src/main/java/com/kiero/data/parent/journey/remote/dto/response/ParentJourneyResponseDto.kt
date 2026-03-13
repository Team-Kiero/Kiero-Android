package com.kiero.data.parent.journey.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentJourneyResponseDto(
    /** 오늘 아이의 불피우기 여부 */
    @SerialName("isFireLitToday") val isFireLitToday: Boolean,
    /** 완료 미션 목록 */
    @SerialName("completeMissions") val completeMissions: List<ParentJourneyMissionDto>,
    /** 미완료 미션 목록 */
    @SerialName("incompleteMissions") val incompleteMissions: List<ParentJourneyMissionDto>,
    /** 일정 현황 목록 */
    @SerialName("schedules") val schedules: List<ParentJourneyScheduleDto>
)

@Serializable
data class ParentJourneyMissionDto(
    /** 미션의 이름(내용) */
    @SerialName("name") val name: String,
    /** 보상으로 얻을 수 있는 금화 수 */
    @SerialName("reward") val reward: Int
)

@Serializable
data class ParentJourneyScheduleDto(
    /** 일정 이름 */
    @SerialName("name") val name: String,
    /** 일정 시작 시간 */
    @SerialName("startTime") val startTime: String,
    /** 일정 종료 시간 */
    @SerialName("endTime") val endTime: String,
    /** 일정 상세 id */
    @SerialName("scheduleDetailId") val scheduleDetailId: Long,
    /** 현재 진행 중인 일정 여부 (true면 현재 시간대에 해당하는 일정) */
    @SerialName("isOngoing") val isOngoing: Boolean,
    /** 일정 상태 ENUM */
    @SerialName("status") val status: String
)
