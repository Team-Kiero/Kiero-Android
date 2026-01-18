package com.kiero.data.alarm.repository

import com.kiero.data.alarm.model.AlarmFeedModel
import com.kiero.data.alarm.model.AlarmItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AlarmRepository {

    val cachedAlarms: StateFlow<List<AlarmItemModel>>
    val childName: StateFlow<String>
    val nextCursor: StateFlow<String?>

    suspend fun loadAlarms(
        childId: Long,
        size: Int = 20,
        refresh: Boolean = false
    ): Result<AlarmFeedModel>

    suspend fun loadMore(
        childId: Long,
        size: Int = 20
    ): Result<AlarmFeedModel>

    fun subscribeAlarmFeed(childId: Long): Flow<AlarmItemModel>

    suspend fun addNewAlarm(item: AlarmItemModel)

    fun clearCache()
}