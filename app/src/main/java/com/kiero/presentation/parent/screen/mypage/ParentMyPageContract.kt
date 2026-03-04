package com.kiero.presentation.parent.screen.mypage

import com.kiero.core.model.parent.ParentInfo

data class ParentMyPageState(
    val parentInfo : ParentInfo = ParentInfo()
)

sealed interface ParentMyPageSideEffect {
    data class ShowToast(val message : String) : ParentMyPageSideEffect
    data class ShowSnackBar(val message : String) : ParentMyPageSideEffect
}
