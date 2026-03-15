package com.kiero.data.kid.schedule.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.kid.schedule.model.ScheduleFireModel
import com.kiero.data.kid.schedule.model.ScheduleTodayModel
import com.kiero.data.kid.schedule.model.toModel
import com.kiero.data.kid.schedule.remote.datasource.ScheduleDataSource
import com.kiero.data.kid.schedule.repository.ScheduleRepository
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val dataSource: ScheduleDataSource
) : ScheduleRepository {
    override suspend fun patchScheduleToday(): Result<ScheduleTodayModel> = suspendRunCatching {
        dataSource.patchScheduleToday().data!!.toModel()
    }

    override suspend fun patchScheduleComplete(
        scheduleDetailId: Long,
        imageUrl: String
    ): Result<Unit> = suspendRunCatching {
        dataSource.patchScheduleComplete(
            scheduleDetailId = scheduleDetailId,
            imageUrl = imageUrl
        )
        Unit
    }

    override suspend fun patchScheduleSkip(
        scheduleDetailId: Long
    ): Result<Unit> = suspendRunCatching {
        dataSource.patchScheduleSkip(scheduleDetailId)
        Unit
    }

    override suspend fun patchScheduleFireLit(
    ): Result<ScheduleFireModel> = suspendRunCatching {
        dataSource.patchScheduleFireLit().data!!.toModel()
    }
}