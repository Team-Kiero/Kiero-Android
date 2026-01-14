package com.kiero.presentation.kid.journey.util

import com.kiero.presentation.kid.journey.model.KidJourneyContentModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleModel

object KidJourneyContentUtil {

    fun getScheduleInfo(content: KidJourneyContentModel): KidJourneyScheduleModel? {
        return when (content) {
            is KidJourneyContentModel.FirstSchedule -> content.scheduleInfo
            is KidJourneyContentModel.NowSchedule -> content.scheduleInfo
            is KidJourneyContentModel.NextSchedule -> content.scheduleInfo
            else -> null
        }
    }
}