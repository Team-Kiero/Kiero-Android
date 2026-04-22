package com.kiero.presentation.parent.screen.journey

import androidx.constraintlayout.compose.DesignElements.map
import com.kiero.data.parent.journey.model.ParentJourneyScheduleModel
import com.kiero.presentation.parent.screen.journey.extension.toLocalTime
import com.kiero.presentation.parent.screen.journey.model.JourneyMissionUiModel
import com.kiero.presentation.parent.screen.journey.model.KidInfo
import com.kiero.presentation.parent.screen.journey.model.TodayJourneyUiModel
import com.kiero.presentation.parent.screen.journey.model.TodayStatus
import com.kiero.presentation.parent.screen.journey.model.toUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalTime

data class ParentJourneyState(
    val kidInfo: KidInfo = KidInfo(),
    val isFireLitToday: Boolean = false,

    val completeMissions: ImmutableList<JourneyMissionUiModel> = persistentListOf(),
    val incompleteMissions: ImmutableList<JourneyMissionUiModel> = persistentListOf(),
    val todayMissionList: PersistentList<TodayJourneyUiModel> = persistentListOf(),

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

// 현재일정 , 다음일정 텍스트O
fun List<ParentJourneyScheduleModel>.toUiModels(currentTime: LocalTime): List<TodayJourneyUiModel> {
    // isOngoing=true이면서 PENDING/VERIFIED/COMPLETED인 경우만 현재 진행 중으로 인정
    // SKIPPED/FAILED는 isOngoing=true여도 종료된 것으로 처리
    val activeOngoing = find {
        it.isOngoing && it.status != "SKIPPED" && it.status != "FAILED"
    }
    val hasOngoingSchedule = activeOngoing != null

    val nextUpcomingId = if (hasOngoingSchedule) null
    else filter { schedule ->
        val start = schedule.startTime.toLocalTime()
        schedule.status == "PENDING" && start != null && start.isAfter(currentTime)
    }
        .minByOrNull { it.startTime }
        ?.scheduleDetailId

    return map { schedule ->
        schedule.toUiModel(
            currentTime = currentTime,
            isNextUpcoming = schedule.scheduleDetailId == nextUpcomingId,
            hasOngoingSchedule = hasOngoingSchedule
        )
    }
}

// 현재일정 , 다음일정 텍스트x
/*fun List<ParentJourneyScheduleModel>.toUiModels(currentTime: LocalTime): List<TodayJourneyUiModel> {
    // 현재 진행 중인 일정이 있는지 확인
    val hasOngoingSchedule = any { schedule ->
        val start = schedule.startTime.toLocalTime()
        val end = schedule.endTime.toLocalTime()
        start != null && end != null && currentTime >= start && currentTime < end
    }

    // 진행 중인 일정이 없을 때만 다음 일정 id 찾기
    val nextUpcomingId = if (hasOngoingSchedule) {
        null
    } else {
        filter { schedule ->
            val start = schedule.startTime.toLocalTime()
            start != null && start.isAfter(currentTime)
        }
            .minByOrNull { it.startTime }
            ?.scheduleDetailId
    }

    return map { schedule ->
        schedule.toUiModel(
            currentTime = currentTime,
            isNextUpcoming = schedule.scheduleDetailId == nextUpcomingId
        )
    }
}*/

sealed interface ParentJourneySideEffect {
    data object NavigateUp : ParentJourneySideEffect

    data class ShowSnackbar(val message: String) : ParentJourneySideEffect
}
