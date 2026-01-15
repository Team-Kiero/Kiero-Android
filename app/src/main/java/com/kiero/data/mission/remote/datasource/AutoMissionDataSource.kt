package com.kiero.data.mission.remote.datasource

// TODO: DTO 파일 생성 후 임포트
// import com.kiero.data.mission.dto.AnalyzeNoticeRequestDto
// import com.kiero.data.mission.dto.AnalyzeNoticeResponseDto
// import com.kiero.data.mission.dto.BatchMissionRequestDto
// import com.kiero.data.mission.dto.BatchMissionResponseDto

/**
 * [Data Layer - DataSource]
 *
 * 역할:
 * - Retrofit Service와 1:1 매핑
 * - 순수 API 호출만 담당
 * - DTO 입력받고 DTO 반환
 *
 * TODO: 구현 필요
 * 1. MissionApiService.kt 작성 (Retrofit)
 * 2. MissionRemoteDataSourceImpl.kt 작성
 * 3. NetworkModule에서 Hilt 바인딩
 */
interface AutoMissionDataSource {

    /**
     * [SCH-007] 알림장 분석 API
     *
     * API 스펙:
     * - Endpoint: POST /missions/recommend
     * - Request Body: { "noticeText": "..." }
     * - Response: { "status": 200, "data": { "suggestedMissions": [...] } }
     *
     * TODO: Retrofit 연동
     * ```kotlin
     * @POST("/missions/recommend")
     * suspend fun analyzeNotice(
     *     @Body request: AnalyzeNoticeRequestDto
     * ): AnalyzeNoticeResponseDto
     * ```
     */
    suspend fun analyzeNotice(
        request: Any  // TODO: AnalyzeNoticeRequestDto로 변경
    ): Any  // TODO: AnalyzeNoticeResponseDto로 변경

    /**
     * [SCH-008] 미션 일괄 생성 API
     *
     * API 스펙:
     * - Endpoint: POST /missions/{childId}
     * - Path Variable: childId (Long)
     * - Request Body: { "missions": [...] }
     * - Response: { "status": 201, "data": [...] }
     *
     * TODO: Retrofit 연동
     * ```kotlin
     * @POST("/missions/{childId}")
     * suspend fun saveBatchMissions(
     *     @Path("childId") childId: Long,
     *     @Body request: BatchMissionRequestDto
     * ): BatchMissionResponseDto
     * ```
     */
    suspend fun saveBatchMissions(
        childId: Long,
        request: Any  // TODO: BatchMissionRequestDto로 변경
    ): Any  // TODO: BatchMissionResponseDto로 변경
}