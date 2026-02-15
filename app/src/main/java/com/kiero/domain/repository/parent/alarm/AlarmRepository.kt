package com.kiero.domain.repository.parent.alarm

import com.kiero.domain.entity.parent.alarm.AlarmFeedModel
import com.kiero.domain.entity.parent.alarm.AlarmItemModel
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

    fun clearCache()
}