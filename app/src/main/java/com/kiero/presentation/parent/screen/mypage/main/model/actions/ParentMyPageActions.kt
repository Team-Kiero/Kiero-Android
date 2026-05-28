package com.kiero.presentation.parent.screen.mypage.main.model.actions

import androidx.compose.runtime.Stable
import com.kiero.presentation.parent.screen.mypage.main.model.ParentMenuLinkType

@Stable
interface ParentMyPageActions {
    fun onClickChildCare()
    fun onClickLogOut()
    fun onClickLogOutConfirm()
    fun onClickLogOutCancel()
    fun onClickWithDraw()
    fun onClickOss()
    fun onCheckedChange(checked: Boolean)
    fun onClickTerms(type: ParentMenuLinkType)
}