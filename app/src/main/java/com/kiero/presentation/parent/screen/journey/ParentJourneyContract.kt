package com.kiero.presentation.parent.screen.journey

import com.kiero.data.parent.journey.model.ParentJourneyScheduleModel
import com.kiero.presentation.parent.screen.journey.extension.toLocalTime
import com.kiero.presentation.parent.screen.journey.model.JourneyMissionUiModel
import com.kiero.presentation.parent.screen.journey.model.KidInfo
import com.kiero.presentation.parent.screen.journey.model.TodayJourneyUiModel
import com.kiero.presentation.parent.screen.journey.model.TodayStatus
import com.kiero.presentation.parent.screen.journey.model.toUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalTime

data class ParentJourneyState(
    val kidInfo: KidInfo = KidInfo(),
    val isFireLitToday: Boolean = false,

    val completeMissions: ImmutableList<JourneyMissionUiModel> = persistentListOf(),
    val incompleteMissions: ImmutableList<JourneyMissionUiModel> = persistentListOf(),
    val todayMissionList: ImmutableList<TodayJourneyUiModel> = persistentListOf(),

    val selectedJourneyImageUrl: String = ""
) {
    companion object {
        val FAKE = persistentListOf(
            TodayJourneyUiModel(
                date = "2024-05-20",
                todayMission = "아침 먹고 스스로 양치하기",
                todayStatus = TodayStatus.PAST_COMPLETED
            ),
            TodayJourneyUiModel(
                date = "2024-05-21",
                todayMission = "강아지와 10분 동안 산책하기",
                todayStatus = TodayStatus.PAST_MISSED
            ),
            TodayJourneyUiModel(
                date = "2024-05-22",
                todayMission = "잠자기 전 동화책 한 권 읽기",
                todayStatus = TodayStatus.CURRENT_COMPLETED
            ),
            TodayJourneyUiModel(
                date = "2024-05-23",
                todayMission = "가지고 논 장난감 제자리에 두기",
                todayStatus = TodayStatus.UPCOMING
            ),
            TodayJourneyUiModel(
                date = "2024-05-24",
                todayMission = "부모님 심부름 한 가지 도와드리기",
                todayStatus = TodayStatus.UPCOMING
            ),
            TodayJourneyUiModel(
                date = "2024-05-2",
                todayMission = "아침 먹고 스스로 양치하기",
                todayStatus = TodayStatus.TODAY_COMPLETED
            ),
        )
    }
}

fun List<ParentJourneyScheduleModel>.toUiModels(currentTime: LocalTime): List<TodayJourneyUiModel> {
    // PENDING 중 현재 시간 이후 가장 빠른 일정 id 찾기
    val nextUpcomingId = filter { it.status == "PENDING" }
        .filter { it.startTime.toLocalTime()?.isAfter(currentTime) == true }
        .minByOrNull { it.startTime }
        ?.scheduleDetailId

    return map { schedule ->
        schedule.toUiModel(
            currentTime = currentTime,
            isNextUpcoming = schedule.scheduleDetailId == nextUpcomingId
        )
    }
}

sealed interface ParentJourneySideEffect {
    data object NavigateUp : ParentJourneySideEffect

    data class ShowSnackbar(val message: String) : ParentJourneySideEffect
}
