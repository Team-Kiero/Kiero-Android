package com.kiero.presentation.parent.screen.mission.directadd.model

data class MissionAwardValue(
    val value: Int
) {

    val displayText: String
        get() = if (value > 0) "+$value" else "$value"
}

object MissionAwardDefaults {
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

enum class MissionAddValid(val message: String) {
    MISSION(message = "미션 이름을 입력해주세요"),
    AWARD(message = "보상을 입력해주세요"),
    MAX(message = "최대 보상은 500개입니다.")
}