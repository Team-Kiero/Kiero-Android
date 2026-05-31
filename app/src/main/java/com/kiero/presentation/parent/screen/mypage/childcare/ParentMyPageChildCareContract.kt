package com.kiero.presentation.parent.screen.mypage.childcare

import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentChildCareStep
import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentMyPageChildInfoModel
import com.kiero.presentation.parent.screen.mypage.model.ChildConnectionStatus

data class ParentMyPageChildCareState(
    val childInfo: ParentMyPageChildInfoModel = ParentMyPageChildInfoModel(),
    val expiredTime: String = "",
    val currentStep: ParentChildCareStep = ParentChildCareStep.MANAGEMENT,
    val connectionStatus: ChildConnectionStatus = ChildConnectionStatus.CONNECTED,
    val isLogoutDialogVisible: Boolean = false,
    val isExpired: Boolean = false,
    val isLoading: Boolean = false,
)

sealed interface ParentMyPageChildCareSideEffect {
    data object NavigateToMyPage : ParentMyPageChildCareSideEffect
    data class CopyText(val message: String, val targetText: String) : ParentMyPageChildCareSideEffect
    data class ShowSnackbar(val message: String) : ParentMyPageChildCareSideEffect
}
