package com.kiero.presentation.parent.screen.journey

import com.kiero.presentation.parent.screen.journey.model.KidInfo
import com.kiero.presentation.parent.screen.journey.model.TodayJourneyModel
import com.kiero.presentation.parent.screen.journey.model.TodayStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ParentJourneyState(
    val kidInfo: KidInfo = KidInfo(),

    val completedMission: Int = 0,
    val unCompletedMission: Int = 0,

    val todayMissionList: ImmutableList<TodayJourneyModel> = persistentListOf()
) {
    companion object {
        val FAKE = persistentListOf(
            TodayJourneyModel(
                date = "2024-05-20",
                todayMission = "아침 먹고 스스로 양치하기",
                todayStatus = TodayStatus.PAST_COMPLETED
            ),
            TodayJourneyModel(
                date = "2024-05-21",
                todayMission = "강아지와 10분 동안 산책하기",
                todayStatus = TodayStatus.PAST_MISSED
            ),
            TodayJourneyModel(
                date = "2024-05-22",
                todayMission = "잠자기 전 동화책 한 권 읽기",
                todayStatus = TodayStatus.CURRENT_COMPLETED
            ),
            TodayJourneyModel(
                date = "2024-05-23",
                todayMission = "가지고 논 장난감 제자리에 두기",
                todayStatus = TodayStatus.UPCOMING
            ),
            TodayJourneyModel(
                date = "2024-05-24",
                todayMission = "부모님 심부름 한 가지 도와드리기",
                todayStatus = TodayStatus.UPCOMING
            ),
            TodayJourneyModel(
                date = "2024-05-2",
                todayMission = "아침 먹고 스스로 양치하기",
                todayStatus = TodayStatus.TODAY_COMPLETED
            ),
        )
    }
}

sealed interface ParentJourneySideEffect {
    object NavigateUp : ParentJourneySideEffect
}
