package com.kiero.presentation.parent.schedule.mission.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import java.util.UUID

@Immutable
data class MissionUiModel(
    /**
     * [공통] 데이터 식별 프로퍼티
     */
    val id: Long = 0L,  // 서버에서 할당받는 고유 ID
    // TODO: AI 분석 결과를 받을 때 고유 ID 생성 로직 추가 필요
    val tempId: String = UUID.randomUUID().toString(),

    val name: String = "",           // 미션 이름 (최대 15자)
    val dueAt: LocalDate = LocalDate.now(),  // 마감일
    val reward: Int = 20,            // 보상 금화 (1 ~ 500)
    val isCompleted: Boolean = false,

    /**
     * [공통] 실시간 유효성 검사 결과
     */
    val nameError: String? = null,
    val dateError: String? = null,
    val rewardError: String? = null
) {
    /**
     * 유효성 검사 헬퍼 함수
     */
    fun validate(): MissionUiModel {
        val nError = when {
            name.isBlank() -> "미션 이름을 입력해주세요."
            name.length > 15 -> "미션 이름은 15자 이하로 입력해주세요."
            else -> null
        }
        val dError = if (dueAt.isBefore(LocalDate.now())) {
            "마감일은 과거로 설정할 수 없습니다."
        } else null

        val rError = if (reward <= 0) {
            "보상을 입력해주세요."
        } else null

        return this.copy(
            nameError = nError,
            dateError = dError,
            rewardError = rError
        )
    }

    fun hasError(): Boolean =
        nameError != null || dateError != null || rewardError != null
}