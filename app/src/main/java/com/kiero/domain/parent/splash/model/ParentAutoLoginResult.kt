package com.kiero.domain.parent.splash.model

sealed interface ParentAutoLoginResult {
    //data object MoveToTermsAgreement : ParentAutoLoginResult
    data object MoveToParentHome : ParentAutoLoginResult
    data object MoveToOnboarding : ParentAutoLoginResult // parentGraph로 이동
    data object MoveToAuth : ParentAutoLoginResult
}