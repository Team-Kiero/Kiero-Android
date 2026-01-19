package com.kiero.data.schedule.repository

import com.kiero.data.schedule.model.ScheduleImageUploadModel
import com.kiero.data.schedule.model.TodayScheduleModel

interface ScheduleRepository {
    suspend fun getTodaySchedule(): Result<TodayScheduleModel>

    suspend fun postPresignedUrl(
        fileName: String,
        contentType: String
    ): Result<ScheduleImageUploadModel>

    suspend fun patchScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): Result<Unit>
}