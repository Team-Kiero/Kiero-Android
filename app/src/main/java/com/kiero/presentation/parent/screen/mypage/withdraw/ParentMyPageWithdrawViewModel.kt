package com.kiero.presentation.parent.screen.mypage.withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.analytic.tracker.Tracker
import com.kiero.core.app.AppRestarter
import com.kiero.domain.parent.mypage.WithdrawUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentMyPageWithdrawViewModel @Inject constructor(
    private val appRestarter: AppRestarter,
    private val withdrawUseCase: WithdrawUseCase,
    private val tracker: Tracker
) : ViewModel() {
    fun withdraw() {
        viewModelScope.launch {
            withdrawUseCase()
                .onSuccess {
                    tracker.reset()
                    appRestarter.restartApp()
                }
                .onFailure {
                    Timber.e(it, "탈퇴 실패")
                }
        }
    }
}
