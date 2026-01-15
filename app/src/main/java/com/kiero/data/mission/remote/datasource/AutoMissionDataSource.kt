package com.kiero.data.mission.remote.datasource

// TODO: DTO 파일 생성 후 임포트

/**
 * TODO: 구현 필요
 * 1. MissionApiService.kt 작성 (Retrofit)
 * 2. MissionRemoteDataSourceImpl.kt 작성
 * 3. NetworkModule에서 Hilt 바인딩
 */
interface AutoMissionDataSource {

    suspend fun analyzeNotice(
        request: Any  // TODO: AnalyzeNoticeRequestDto로 변경
    ): Any  // TODO: AnalyzeNoticeResponseDto로 변경


    suspend fun saveBatchMissions(
        childId: Long,
        request: Any  // TODO: BatchMissionRequestDto로 변경
    ): Any  // TODO: BatchMissionResponseDto로 변경
}