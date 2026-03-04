package com.kiero.presentation.kid.journey.map.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.journey.map.model.KidMapItemModel

@Immutable
data class KidMapState(
    val date: String = "",
    val scheduleCount: Int = 0,
    val schedules: List<KidMapItemModel> = emptyList()
) {
    companion object {
        fun fake() = KidMapState(
            scheduleCount = 4,
            schedules = listOf(
                KidMapItemModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = com.kiero.presentation.kid.journey.model.KidJourneyStoneType.COURAGE,
                    status = com.kiero.presentation.kid.journey.map.model.MapScheduleStatus.COMPLETE
                ),
                KidMapItemModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = true,
                    stoneType = com.kiero.presentation.kid.journey.model.KidJourneyStoneType.GRIT,
                    status = com.kiero.presentation.kid.journey.map.model.MapScheduleStatus.PENDING
                ),
                KidMapItemModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = com.kiero.presentation.kid.journey.model.KidJourneyStoneType.WISDOM,
                    status = com.kiero.presentation.kid.journey.map.model.MapScheduleStatus.PENDING
                ),
                KidMapItemModel(
                    name = "꾸비 성수팝업 가기",
                    startTime = "오후 04:00",
                    endTime = "오후 05:00",
                    isOngoing = false,
                    stoneType = com.kiero.presentation.kid.journey.model.KidJourneyStoneType.COURAGE,
                    status = com.kiero.presentation.kid.journey.map.model.MapScheduleStatus.PENDING
                )
            )
        )
    }
}