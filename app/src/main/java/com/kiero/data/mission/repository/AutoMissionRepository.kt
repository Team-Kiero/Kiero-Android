package com.kiero.data.mission.repository

import com.kiero.presentation.parent.schedule.mission.model.MissionUiModel

/**
 * [Data Layer - Repository]
 *
 * 역할:
 * 1. DataSource 호출
 * 2. DTO ↔ UiModel 변환 (날짜 문자열 ↔ LocalDate)
 * 3. 네트워크 정책 (15초 타임아웃)
 * 4. 에러 처리 및 매핑
 * 5. Result 타입으로 성공/실패 전달
 *
 * 참고: Domain 레이어 생략으로 UiModel 직접 사용
 *
 * TODO: 구현 필요
 * 1. MissionRepositoryImpl.kt 작성
 * 2. DataModule에서 Hilt 바인딩
 */
interface AutoMissionRepository {

    /**
     * [SCH-007] 알림장 분석
     *
     * 처리 흐름:
     * 1. noticeText → AnalyzeNoticeRequestDto 생성
     * 2. withTimeout(15000L) { dataSource.analyzeNotice(...) }
     * 3. AnalyzeNoticeResponseDto → List<MissionUiModel> 변환
     *    - dto.dueAt (String "2026-01-16") → LocalDate
     * 4. Result.success(missions) 반환
     * ```
     *
     * @param noticeText 사용자 입력 알림장 텍스트 (10~1000자)
     * @return Result<List<MissionUiModel>> 성공 시 미션 리스트
     */
    suspend fun analyzeNotice(
        noticeText: String
    ): Result<List<MissionUiModel>>

    /**
     * [SCH-008] 미션 일괄 생성
     *
     * 처리 흐름:
     * 1. List<MissionUiModel> → BatchMissionRequestDto 변환
     *    - mission.dueAt (LocalDate) → String "2026-01-16"
     * 2. dataSource.saveBatchMissions(childId, request)
     * 3. 403 에러 → PermissionException("해당 자녀에 대한 권한이 없습니다.")
     * 4. Result.success(Unit) 반환
     *
     * ```
     *
     * @param childId 자녀 ID
     * @param missions 저장할 미션 리스트
     * @return Result<Unit> 성공 시 Unit
     */
    suspend fun saveBatchMissions(
        childId: Long,
        missions: List<MissionUiModel>
    ): Result<Unit>
}