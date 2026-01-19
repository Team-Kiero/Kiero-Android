package com.kiero.data.schedule.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.schedule.datasource.ScheduleDataSource
import com.kiero.data.schedule.model.ScheduleFireModel
import com.kiero.data.schedule.model.ScheduleImageUploadModel
import com.kiero.data.schedule.model.ScheduleSkipModel
import com.kiero.data.schedule.model.ScheduleTodayModel
import com.kiero.data.schedule.model.toModel
import com.kiero.data.schedule.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val dataSource: ScheduleDataSource
) : ScheduleRepository {
    override suspend fun patchScheduleToday(): Result<ScheduleTodayModel> = suspendRunCatching {
        dataSource.patchScheduleToday().data!!.toModel()
    }

    override suspend fun postPresignedUrl(
        fileName: String,
        contentType: String
    ): Result<ScheduleImageUploadModel> = suspendRunCatching {
        dataSource.postPresignedUrl(
            fileName = fileName,
            contentType = contentType
        ).data!!.toModel()
    }

    override suspend fun patchScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): Result<Unit> = suspendRunCatching {
        dataSource.patchScheduleComplete(
            scheduleDetailId = scheduleDetailId,
            imageUrl = imageUrl
        ).data!!
    }

    override suspend fun patchScheduleSkip(
        scheduleDetailId: Long
    ): Result<ScheduleSkipModel> = suspendRunCatching {
        dataSource.patchScheduleSkip(scheduleDetailId).data!!.toModel()
    }

    override suspend fun patchScheduleFireLit(
    ): Result<ScheduleFireModel> = suspendRunCatching {
        dataSource.patchScheduleFireLit().data!!.toModel()
    }
}