package com.kiero.presentation.parent.screen.mypage.main

import com.kiero.core.model.parent.ParentInfo
import com.kiero.presentation.parent.screen.mypage.main.model.ParentMyPageMenuUiModel
import com.kiero.presentation.parent.screen.mypage.model.ChildConnectionStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ParentMyPageState(
    val parentInfo : ParentInfo = ParentInfo(),
    val myPageMenus : ImmutableList<ParentMyPageMenuUiModel> = persistentListOf(),
    val connectionStatus: ChildConnectionStatus = ChildConnectionStatus.REISSUE,
    val isAlarmChecked: Boolean = false
)

sealed interface ParentMyPageSideEffect {
    data class ShowToast(val message : String) : ParentMyPageSideEffect
    data class ShowSnackBar(val message : String) : ParentMyPageSideEffect

    data object NavigateToChildCare: ParentMyPageSideEffect
    data object NavigateToWithDraw : ParentMyPageSideEffect
}
