package com.kiero.presentation.kid.mission.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.mission.model.KidMissionByDateUiModel
import com.kiero.presentation.kid.mission.model.KidMissionUiModel
import com.kiero.presentation.kid.model.KidCoinUiModel

@Immutable
data class KidMissionState(
    val coinUiModel: KidCoinUiModel = KidCoinUiModel(),

    // 각 미션 섹션에 대한 정보
    val kidMissionByDateList: KidMissionByDateUiModel = KidMissionByDateUiModel(),

    val isRefreshing : Boolean = false,
    val isVisibleDialog: Boolean = false,
    val isCompletedMission: Boolean = false,
    val selectedMissionItem: KidMissionUiModel? = null
) {
    val kidName: String
        get() = coinUiModel.firstName
}

sealed interface KidMissionSideEffect {
    data class ShowSnackbar(val message : String) : KidMissionSideEffect
}