package com.kiero.presentation.parent.alarm.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


@Immutable
data class ParentAlarmUiModel(
    /** LazyColumn에서 item key로 사용. 스크롤 시 상태 보존용 고유 식별자 */
    val id: String,

    /** 화면에 표시할 시간. 예: "오늘 12 : 00", "어제 14 : 30" */
    val time: String,

    /** 화면에 표시할 완성된 메시지. 예: "근영이가 피아노 학원에 도착했어요." */
    val message: String,

    /** message 중 강조 표시할 텍스트. 예: "피아노 학원" */
    val highlightText: String,

    /** highlightText에 적용할 색상. 이벤트 타입별로 다름 */
    val highlightColor: Color,

    /** 코인 사용 개수. null이면 코인 정보 표시 안 함 */
    val coinUsed: Int?,

    /** 이미지 URL. null이면 드롭다운 불가능, 값이 있으면 드롭다운 가능 */
    val imageUrl: String?,

    /** 현재 확장 상태. true면 이미지 표시, false면 숨김 */
    val isExpanded: Boolean = false
)