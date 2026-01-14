package com.kiero.data.alarm.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.alarm.datasource.AlarmDataSource
import com.kiero.data.alarm.model.AlarmFeedModel
import com.kiero.data.alarm.model.AlarmItemModel
import com.kiero.data.alarm.model.toModel
import com.kiero.data.alarm.repository.AlarmRepository
import com.kiero.data.auth.local.datasource.AuthLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton  // 앱 전체에서 하나만 (캐시 공유)
class AlarmRepositoryImpl @Inject constructor(
    private val dataSource: AlarmDataSource,
    private val authLocalDataSource: AuthLocalDataSource
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

    // 토큰 생성 로직 공통화
    private suspend fun getBearerToken(): String =
        "Bearer ${authLocalDataSource.getAccessToken()}"

    // API 호출 및 공통 처리를 위한 내부 전용 함수
    private suspend fun fetchFeeds(
        childId: Long,
        size: Int,
        cursor: String?
    ): AlarmFeedModel {
        val response = dataSource.getAlarmFeed(
            token = getBearerToken(),
            childId = childId,
            size = size,
            cursor = cursor
        )
        return response.data!!.toModel()
    }

    override suspend fun loadAlarms(
        childId: Long,
        size: Int,
        refresh: Boolean
    ): Result<AlarmFeedModel> = suspendRunCatching {
        Timber.d("loadAlarms - childId: $childId, size: $size, refresh: $refresh")

        val model = fetchFeeds(childId, size, null) // 초기 로드는 cursor null

        // 상태 업데이트 (새 데이터로 교체)
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

        // 상태 업데이트 (기존 데이터에 추가 및 용량 제한)
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

    /**
     * 실시간 알람 구독 (SSE) 구현
     */
    override fun subscribeAlarmFeed(childId: Long): Flow<AlarmItemModel> = flow {
        val token = getBearerToken()

        // DataSource의 스트림을 collect하여 실시간으로 데이터 수신
        dataSource.subscribeAlarmFeed(token, childId).collect { dto ->
            dto.toModel()?.let { model ->
                // 캐시 리스트에 즉시 반영
                addNewAlarm(model)
                // 필요 시 외부(ViewModel)로 데이터 전달
                emit(model)
            }
        }
    }.flowOn(Dispatchers.IO) // 네트워크 스트림 읽기는 IO 스레드에서

    override suspend fun addNewAlarm(item: AlarmItemModel) {
        // TODO: Paging 3의 invalidate() 사용으로 대체 가능
        Timber.d("addNewAlarm - item: $item")

        _cachedAlarms.update { current ->
            // ✅ 중복 체크: 이미 동일한 id(feedItemId)가 있으면 리스트 유지, 없으면 맨 앞에 추가
            if (current.any { it.id == item.id }) {
                current
            } else {
                listOf(item) + current
            }
        }
    }

    override fun clearCache() {
        Timber.d("clearCache")

        _cachedAlarms.value = emptyList()
        _childName.value = ""
        _nextCursor.value = null
    }
}