package com.kiero.data.alarm.repository

import com.kiero.data.alarm.model.AlarmFeedModel
import com.kiero.data.alarm.model.AlarmItemModel
import kotlinx.coroutines.flow.StateFlow

interface AlarmRepository {
    // TODO: 알람이 100개 이상으로 많아지면 Paging 3 도입 검토
    //  - androidx.paging:paging-runtime 사용
    //  - PagingSource 구현
    //  - cachedAlarms를 Flow<PagingData<AlarmItemModel>>로 변경

    /**
     * 캐시된 알람 리스트 (메모리)
     * ViewModel에서 구독하여 자동 업데이트
     */
    val cachedAlarms: StateFlow<List<AlarmItemModel>>

    /**
     * 캐시된 childName
     */
    val childName: StateFlow<String>

    /**
     * 다음 커서 (페이징용)
     */
    val nextCursor: StateFlow<String?>

    /**
     * 알람 피드 로드
     * @param childId 조회할 자녀 ID
     * @param size 한 번에 조회할 크기 (기본 20)
     * @param refresh true면 캐시 초기화 후 로드
     * @return 알람 피드 도메인 모델
     */
    suspend fun loadAlarms(
        childId: Long,
        size: Int = 20, //기본값을 인터페이스에 명시하여 일관성 확보
        refresh: Boolean = false
    ): Result<AlarmFeedModel>

    /**
     * 다음 페이지 로드 (무한 스크롤)
     * @param childId 조회할 자녀 ID
     * @param size 한 번에 조회할 크기 (기본 20)
     * @return 알람 피드 도메인 모델
     */
    suspend fun loadMore(
        childId: Long,
        size: Int = 20 // 기본값 명시
    ): Result<AlarmFeedModel>

    /**
     * SSE로 받은 실시간 알람 추가
     * @param item 새로운 알람 아이템
     */
    suspend fun addNewAlarm(item: AlarmItemModel)

    /**
     * 캐시 초기화
     */
    fun clearCache()
}