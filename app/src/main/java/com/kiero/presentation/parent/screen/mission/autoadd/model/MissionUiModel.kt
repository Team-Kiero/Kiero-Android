package com.kiero.presentation.parent.screen.mission.autoadd.model

import java.time.LocalDate

data class MissionUiModel(
    val id: Long? = null,
    val name: String,
    val reward: Int,
    val dueAt: LocalDate,
    val isCompleted: Boolean = false
)

fun List<MissionUiModel>.updateAt(
    index: Int,
    transform: (MissionUiModel) -> MissionUiModel
): List<MissionUiModel> =
    toMutableList().apply {
        if (index in indices) {
            this[index] = transform(this[index])
        }
    }

fun MissionUiModel.errorMessage(): String? = when {
    name.isBlank() -> "미션 이름을 입력해주세요."
    dueAt.isBefore(LocalDate.now()) -> "마감일은 과거로 설정할 수 없습니다."
    reward <= 0 -> "보상을 입력해주세요."
    else -> null
}