package com.kiero.presentation.parent.screen.mission.autoadd.model

import com.kiero.presentation.parent.screen.mission.directadd.model.MissionAwardValue
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

object AutoMissionAwardDefaults {
    const val MIN_AWARD = 1
    const val MAX_AWARD = 500
    const val DEFAULT_AWARD = 20

    val PRESET_VALUES = listOf(
        MissionAwardValue(-10),
        MissionAwardValue(-5),
        MissionAwardValue(5),
        MissionAwardValue(10)
    )

    fun applyChange(currentValue: Int, change: Int): Int {
        return (currentValue + change).coerceIn(MIN_AWARD, MAX_AWARD)
    }

    fun constrainValue(value: Int): Int {
        return value.coerceIn(MIN_AWARD, MAX_AWARD)
    }
}