package com.kiero.data.alarm.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.alarm.datasource.AlarmDataSource
import com.kiero.data.alarm.model.AlarmFeedModel
import com.kiero.data.alarm.model.AlarmItemModel
import com.kiero.data.alarm.model.toModel
import com.kiero.data.alarm.repository.AlarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    private val dataSource: AlarmDataSource,
) : AlarmRepository {


    private val _cachedAlarms = MutableStateFlow<List<AlarmItemModel>>(emptyList())
    override val cachedAlarms: StateFlow<List<AlarmItemModel>> = _cachedAlarms.asStateFlow()

    private val _childName = MutableStateFlow("")
    override val childName: StateFlow<String> = _childName.asStateFlow()

    private val _nextCursor = MutableStateFlow<String?>(null)
    override val nextCursor: StateFlow<String?> = _nextCursor.asStateFlow()

    private suspend fun fetchFeeds(
        childId: Long,
        size: Int,
        cursor: String?
    ): AlarmFeedModel {
        val response = dataSource.getAlarmFeed(
            childId = childId,
            size = size,
            cursor = cursor
        )
        val model = response.data!!.toModel()
        return model
    }

    override suspend fun loadAlarms(
        childId: Long,
        size: Int,
        refresh: Boolean
    ): Result<AlarmFeedModel> = suspendRunCatching {
        Timber.d("loadAlarms - childId: $childId, size: $size, refresh: $refresh")

        val model = fetchFeeds(childId, size, null)

        _cachedAlarms.value = model.items
        _childName.value = model.childName
        _nextCursor.value = model.nextCursor

        Timber.d("loadAlarms success - items: ${model.items.size}, nextCursor: ${model.nextCursor}")

        model
    }

    override suspend fun loadMore(
        childId: Long,
        size: Int
    ): Result<AlarmFeedModel> = suspendRunCatching {
        val currentCursor = _nextCursor.value ?: throw IllegalStateException("No more data to load")
        Timber.d("loadMore - childId: $childId, cursor: $currentCursor")

        val model = fetchFeeds(childId, size, currentCursor)

        _cachedAlarms.update { current ->
            val newList = current + model.items
            if (newList.size > MAX_CACHE_SIZE) {
                Timber.w("Cache size exceeded, keeping last $MAX_CACHE_SIZE items")
                newList.takeLast(MAX_CACHE_SIZE)
            } else {
                newList
            }
        }
        _nextCursor.value = model.nextCursor

        Timber.d("loadMore success - added: ${model.items.size}, total: ${_cachedAlarms.value.size}")

        model
    }

    override fun clearCache() {
        Timber.d("clearCache")

        _cachedAlarms.value = emptyList()
        _childName.value = ""
        _nextCursor.value = null
    }

    companion object {
        private const val MAX_CACHE_SIZE = 100
    }
}
