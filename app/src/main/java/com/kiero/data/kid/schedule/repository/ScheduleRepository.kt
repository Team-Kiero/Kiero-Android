package com.kiero.data.kid.schedule.repository

import com.kiero.data.kid.schedule.model.ScheduleFireModel
import com.kiero.data.kid.schedule.model.ScheduleTodayModel

interface ScheduleRepository {
    suspend fun patchScheduleToday(): Result<ScheduleTodayModel>

    suspend fun patchScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): Result<Unit>

    suspend fun patchScheduleSkip(
        scheduleDetailId: Long
    ): Result<Unit>

    suspend fun patchScheduleFireLit(): Result<ScheduleFireModel>
}