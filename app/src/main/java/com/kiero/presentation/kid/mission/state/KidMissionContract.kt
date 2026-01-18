package com.kiero.presentation.kid.mission.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.mission.model.KidMissionByDateUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class KidMissionState(
    val kidName: String = "",
    // 각 미션 섹션에 대한 정보
    val kidMissionByDateList: KidMissionByDateUiModel = KidMissionByDateUiModel()
)

sealed interface KidMissionSideEffect {
    data class ShowSnackbar(val message : String) : KidMissionSideEffect
}