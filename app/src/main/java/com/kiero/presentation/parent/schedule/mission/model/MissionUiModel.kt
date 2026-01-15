package com.kiero.presentation.parent.schedule.mission.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import java.util.UUID

@Immutable
data class MissionUiModel(
    /**
     * [공통] 데이터 식별 프로퍼티
     */
    // 서버에서 할당받는 고유 ID (생성 전에는 0 또는 null)
    val id: Long = 0L,
    // [Auto 전용] AI가 생성한 여러 미션 중 리스트 내에서 UI 안정성을 유지하기 위한 임시 키
    val tempId: String = UUID.randomUUID().toString(),

    /**
     * [공통] 핵심 입력 데이터 (Direct / Auto 공용)
     */
    // 미션 이름 (최대 15자)
    val name: String = "",
    // 마감일 (과거 날짜 불가)
    val dueAt: LocalDate = LocalDate.now(),
    // 보상 금화 (1 ~ 500개)
    val reward: Int = 20,
    // 미션 완료 여부 (생성 시 기본 false)
    val isCompleted: Boolean = false,

    /**
     * [공통] 실시간 유효성 검사 결과 (UI 에러 메시지 노출용)
     * 기획서의 #4-3, #12-3 유효성 검사 로직에 대응함
     */
    val nameError: String? = null,
    val dateError: String? = null,
    val rewardError: String? = null,

    /**
     * [Auto 전용] 흐름 제어 프로퍼티
     */
    // [Auto 전용] SCH-008에서 사용자가 해당 미션 페이지를 확인했는지 여부
    // 기획서의 "미션을 전부 확인 후(n/n 되었을 때) 버튼 활성화" 로직 구현에 사용
    val isConfirmed: Boolean = false,

    /**
     * [Direct 전용] UI 제어 프로퍼티
     */
    // [Direct 전용] SCH-005 진입 시 이름 필드에 바로 포커스를 줄지 여부
    val isInitialFocus: Boolean = false
)