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

@Singleton  // 앱 전체에서 하나만 (캐시 공유)
class AlarmRepositoryImpl @Inject constructor(
    private val dataSource: AlarmDataSource
) : AlarmRepository {

    // TODO: Paging 3 전환 시 제거
    //  - RemoteMediator 사용하여 네트워크 + 로컬 DB 동기화

    companion object {
        private const val MAX_CACHE_SIZE = 100  // 메모리 제한
    }

    // 캐시된 알람 리스트
    private val _cachedAlarms = MutableStateFlow<List<AlarmItemModel>>(emptyList())
    override val cachedAlarms: StateFlow<List<AlarmItemModel>> = _cachedAlarms.asStateFlow()

    // 캐시된 childName
    private val _childName = MutableStateFlow("")
    override val childName: StateFlow<String> = _childName.asStateFlow()

    // 다음 커서
    private val _nextCursor = MutableStateFlow<String?>(null)
    override val nextCursor: StateFlow<String?> = _nextCursor.asStateFlow()

    override suspend fun loadAlarms(
        childId: Long,
        size: Int?,
        refresh: Boolean
    ): Result<AlarmFeedModel> = suspendRunCatching {
        Timber.d("loadAlarms - childId: $childId, size: $size, refresh: $refresh")

        val response = dataSource.getAlarmFeed(
            childId = childId,
            size = size,
            cursor = null  // 초기 로드는 항상 null (첫 페이지)
        )

        val model = response.data!!.toModel()

        // 캐시 업데이트
        _cachedAlarms.value = model.items
        _childName.value = model.childName
        _nextCursor.value = model.nextCursor

        Timber.d("loadAlarms success - items: ${model.items.size}, nextCursor: ${model.nextCursor}")

        model
    }

    override suspend fun loadMore(
        childId: Long,
        size: Int?
    ): Result<AlarmFeedModel> = suspendRunCatching {
        val currentCursor = _nextCursor.value
            ?: throw IllegalStateException("No more data to load")

        Timber.d("loadMore - childId: $childId, cursor: $currentCursor")

        val response = dataSource.getAlarmFeed(
            childId = childId,
            size = size,
            cursor = currentCursor
        )

        val model = response.data!!.toModel()

        // 캐시에 추가 (기존 리스트 + 새 리스트)
        _cachedAlarms.update { current ->
            val newList = current + model.items

            // TODO: Paging 3 사용 시 자동으로 메모리 관리됨
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

    override suspend fun addNewAlarm(item: AlarmItemModel) {
        // TODO: Paging 3의 invalidate() 사용으로 대체 가능
        Timber.d("addNewAlarm - item: $item")

        _cachedAlarms.update { current ->
            listOf(item) + current  // 맨 앞에 추가
        }
    }

    override fun clearCache() {
        Timber.d("clearCache")

        _cachedAlarms.value = emptyList()
        _childName.value = ""
        _nextCursor.value = null
    }
}
