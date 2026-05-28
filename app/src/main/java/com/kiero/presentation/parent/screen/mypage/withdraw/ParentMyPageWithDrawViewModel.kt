package com.kiero.presentation.parent.screen.mypage.withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.app.AppRestarter
import com.kiero.domain.parent.mypage.WithDrawUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentMyPageWithDrawViewModel @Inject constructor(
    private val appRestarter: AppRestarter,
    private val withDrawUseCase: WithDrawUseCase
) : ViewModel() {
    fun withDraw() {
        viewModelScope.launch {
            withDrawUseCase()
                .onSuccess {
                    appRestarter.restartApp()
                }
                .onFailure {
                    Timber.e(it, "탈퇴 실패")
                }
        }
    }
}