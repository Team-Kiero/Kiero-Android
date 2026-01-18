package com.kiero.data.schedule.repository

import com.kiero.data.schedule.model.TodayScheduleModel

interface ScheduleRepository {
    suspend fun getTodaySchedule(): Result<TodayScheduleModel>
}