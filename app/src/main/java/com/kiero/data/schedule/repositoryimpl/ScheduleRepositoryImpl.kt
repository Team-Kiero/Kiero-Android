package com.kiero.data.schedule.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.schedule.datasource.ScheduleDataSource
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

}