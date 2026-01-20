package com.kiero.presentation.kid.journey.camera.state

import androidx.compose.runtime.Immutable
import com.kiero.presentation.kid.journey.model.StoneUiType

@Immutable
data class KidCameraState(
    val scheduleDetailId: Long = 0,
    val stoneType: StoneUiType = StoneUiType.WISDOM
)

sealed interface KidCameraSideEffect {
    data object NavigateUp : KidCameraSideEffect
}