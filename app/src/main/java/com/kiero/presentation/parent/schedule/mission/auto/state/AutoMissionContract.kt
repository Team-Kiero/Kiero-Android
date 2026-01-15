package com.kiero.presentation.parent.schedule.mission.auto.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.parent.schedule.mission.model.MissionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

@Immutable
data class AutoMissionContract(
    /** [공통] 전체 로딩 및 에러 상태 */
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    /** [SCH-006] 알림장 입력 데이터 (화면 전환 시 보존 대상) */
    val noticeText: String = "",

    /** [SCH-007] 분석 로딩 상태 (도깨비 캐릭터 화면) */
    val isAnalyzing: Boolean = false,

    /** [SCH-008] AI 분석 결과 리스트 (통합 모델 사용) */
    val missions: ImmutableList<MissionUiModel> = persistentListOf(),

    /** [SCH-008] 현재 확인 중인 미션 인덱스 (0..N-1) */
    val currentIndex: Int = 0,

    /** 최종 일괄 저장 로딩 상태 */
    val isSaving: Boolean = false
) {
    // 분석 버튼 활성화: 공백 제외 10자 이상 1000자 이하
    val isAnalyzeEnabled: Boolean = noticeText.trim().length in 10..1000

    // 현재 선택된 미션 데이터 (편의용)
    val currentMission: MissionUiModel? = missions.getOrNull(currentIndex)

    // 저장 버튼 활성화: 마지막 미션 페이지까지 도달했는지 확인
    val isSaveEnabled: Boolean = missions.isNotEmpty() && currentIndex == missions.size - 1

    companion object Companion {
        // 프리뷰 및 테스트용 가짜 데이터
        val FAKE = AutoMissionContract(
            noticeText = "내일은 독서록을 가져오세요. 수학 익힘책 30쪽까지 풀기.",
            missions = persistentListOf(
                MissionUiModel(
                    name = "독서록 챙기기",
                    dueAt = LocalDate.now().plusDays(1),
                    reward = 20,
                    isConfirmed = true // 첫 페이지는 이미 보고 있음
                ),
                MissionUiModel(
                    name = "수학 익힘책 풀기",
                    dueAt = LocalDate.now().plusDays(1),
                    reward = 50,
                    isConfirmed = false
                )
            ),
            currentIndex = 0
        )
    }
}