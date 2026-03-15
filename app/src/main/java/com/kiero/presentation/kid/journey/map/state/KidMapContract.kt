package com.kiero.presentation.kid.journey.map.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.journey.map.model.KidMapItemUiModel
import com.kiero.presentation.kid.journey.map.model.KidMapScheduleStatus
import com.kiero.presentation.kid.journey.model.KidJourneyStoneType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class KidMapState(
    val date: String = "",
    val scheduleCount: Int = 0,
    val schedules: ImmutableList<KidMapItemUiModel> = persistentListOf()
) {
    companion object {
        val FAKE = KidMapState(
            date = "12월 5일 목요일",
            scheduleCount = 4,
            schedules = persistentListOf(
                KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = KidJourneyStoneType.COURAGE,
                    status = KidMapScheduleStatus.COMPLETE
                ),
                KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = true,
                    stoneType = KidJourneyStoneType.GRIT,
                    status = KidMapScheduleStatus.PENDING
                ),
                KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = KidJourneyStoneType.WISDOM,
                    status = KidMapScheduleStatus.PENDING
                ),
                KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = KidJourneyStoneType.COURAGE,
                    status = KidMapScheduleStatus.PENDING
                ),
                KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = KidJourneyStoneType.COURAGE,
                    status = KidMapScheduleStatus.COMPLETE
                ),
                KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = true,
                    stoneType = KidJourneyStoneType.GRIT,
                    status = KidMapScheduleStatus.PENDING
                ),
                KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = KidJourneyStoneType.WISDOM,
                    status = KidMapScheduleStatus.PENDING
                ),
                KidMapItemUiModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = KidJourneyStoneType.COURAGE,
                    status = KidMapScheduleStatus.PENDING
                ),
            )
        )
    }
}