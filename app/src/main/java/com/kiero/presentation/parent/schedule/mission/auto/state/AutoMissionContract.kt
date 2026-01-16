package com.kiero.presentation.parent.schedule.mission.auto.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.parent.schedule.mission.model.MissionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

@Immutable
data class AutoMissionContract(
    /** 알림장 입력 데이터 (화면 전환 시 보존 대상) */
    val noticeText: String = "",

    /** 분석 로딩 상태 */
    val isAnalyzing: Boolean = false,

    /** AI 분석 결과 리스트 */
    val missions: ImmutableList<MissionUiModel> = persistentListOf(),
    val currentIndex: Int = 0,

    /** 최종 일괄 저장 로딩 상태 */
    val isSaving: Boolean = false,

    val shouldNavigateBack: Boolean = false,
) {
    //  분석 버튼 활성화: 10자 이상 1000자 이하
    val isAnalyzeEnabled: Boolean = noticeText.trim().length in 10..1000

    //  현재 선택된 미션
    val currentMission: MissionUiModel? = missions.getOrNull(currentIndex)

    //  저장 버튼 활성화: 마지막 페이지 도달
    val isSaveEnabled: Boolean = missions.isNotEmpty() &&
            currentIndex == missions.size - 1

    //  현재 표시할 화면 판단
    val currentScreen: Screen
        get() = when {
            isAnalyzing -> Screen.LOADING
            missions.isEmpty() -> Screen.INPUT
            else -> Screen.RESULT
        }

    enum class Screen {
        INPUT,
        LOADING,
        RESULT
    }

    companion object {
        val FAKE = AutoMissionContract(
            noticeText = "내일은 독서록을 가져오세요. 수학 익힘책 30쪽까지 풀기.",
            missions = persistentListOf(
                MissionUiModel(
                    name = "독서록 챙기기",
                    dueAt = LocalDate.now().plusDays(1),
                    reward = 20
                ),
                MissionUiModel(
                    name = "수학 익힘책 풀기",
                    dueAt = LocalDate.now().plusDays(1),
                    reward = 50
                )
            ),
            currentIndex = 0
        )
    }
}