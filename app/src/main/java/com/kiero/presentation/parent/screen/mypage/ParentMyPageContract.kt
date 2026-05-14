package com.kiero.presentation.parent.screen.mypage

import com.kiero.core.model.parent.ParentInfo

data class ParentMyPageState(
    val parentInfo : ParentInfo = ParentInfo(),
    val connectedChildren: Int = 0,
    val isAlarmChecked: Boolean = false
)

sealed interface ParentMyPageSideEffect {
    data class ShowToast(val message : String) : ParentMyPageSideEffect
    data class ShowSnackBar(val message : String) : ParentMyPageSideEffect
}
