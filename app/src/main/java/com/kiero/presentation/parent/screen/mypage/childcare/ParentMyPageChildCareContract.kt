package com.kiero.presentation.parent.screen.mypage.childcare

import com.kiero.presentation.parent.screen.mypage.childcare.model.ParentMyPageChildInfoModel
import com.kiero.presentation.signup.parent.model.ParentSignUpStep

data class ParentMyPageChildCareState(
    val childInfo: ParentMyPageChildInfoModel = ParentMyPageChildInfoModel(),
    val expiredTime: String = "",
    val currentStep: ParentSignUpStep = ParentSignUpStep.ADDCHILD,
    val isLogoutDialogVisible: Boolean = false,
    val isExpired: Boolean = false,
    val isLoading: Boolean = false,
)