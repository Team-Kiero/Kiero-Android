package com.kiero.domain.repository.kid.schedule

import com.kiero.domain.entity.kid.schedule.ScheduleFireModel
import com.kiero.domain.entity.kid.schedule.ScheduleImageUploadModel
import com.kiero.domain.entity.kid.schedule.ScheduleTodayModel

interface ScheduleRepository {
    suspend fun patchScheduleToday(): Result<ScheduleTodayModel>

    suspend fun postPresignedUrl(
        fileName: String,
        contentType: String
    ): Result<ScheduleImageUploadModel>

    suspend fun patchScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): Result<Unit>

    suspend fun patchScheduleSkip(
        scheduleDetailId: Long
    ): Result<Unit>

    suspend fun patchScheduleFireLit(): Result<ScheduleFireModel>
}