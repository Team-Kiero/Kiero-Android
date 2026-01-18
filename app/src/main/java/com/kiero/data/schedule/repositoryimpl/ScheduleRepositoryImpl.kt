package com.kiero.data.schedule.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.schedule.datasource.ScheduleDataSource
import com.kiero.data.schedule.model.ScheduleImageUploadModel
import com.kiero.data.schedule.model.TodayScheduleModel
import com.kiero.data.schedule.model.toModel
import com.kiero.data.schedule.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val dataSource: ScheduleDataSource
) : ScheduleRepository {
    override suspend fun getTodaySchedule(): Result<TodayScheduleModel> = suspendRunCatching {
        dataSource.getTodaySchedule().data!!.toModel()
    }

    override suspend fun getPresignedUrl(
        fileName: String,
        contentType: String
    ): Result<ScheduleImageUploadModel> = suspendRunCatching {
        dataSource.getPresignedUrl(
            fileName = fileName,
            contentType = contentType
        ).data!!.toModel()
    }

    override suspend fun postScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): Result<Unit> = suspendRunCatching {
        dataSource.postScheduleComplete(
            scheduleDetailId = scheduleDetailId,
            imageUrl = imageUrl
        ).data!!
    }
}