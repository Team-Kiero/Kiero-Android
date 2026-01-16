package com.kiero.presentation.kid.journey.util

import com.kiero.presentation.kid.journey.model.KidJourneyContentUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleUiModel

object KidJourneyContentUtil {

    fun getScheduleInfo(content: KidJourneyContentUiModel): KidJourneyScheduleUiModel? {
        return when (content) {
            is KidJourneyContentUiModel.FirstSchedule -> content.scheduleInfo
            is KidJourneyContentUiModel.NowSchedule -> content.scheduleInfo
            is KidJourneyContentUiModel.NextSchedule -> content.scheduleInfo
            else -> null
        }
    }
}