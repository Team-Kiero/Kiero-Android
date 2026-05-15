package com.kiero.presentation.parent.screen.mypage.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.app.AppRestarter
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.parent.ParentInfo
import com.kiero.data.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentMyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userInfoManager: UserInfoManager,
    private val appRestarter: AppRestarter
) : ViewModel() {
    private val _state = MutableStateFlow(ParentMyPageState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentMyPageSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()


    init {
        fetchInfo()
    }

    fun fetchInfo() {
        viewModelScope.launch {
            val kidInfo = userInfoManager.getChildIdInfo()
            val userInfo = userInfoManager.getParentInfo()

            _state.update {
                it.copy(
                    parentInfo = userInfo ?: ParentInfo(),
                    connectedChildren = if (kidInfo != null) 1 else 0
                )
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.postLogout()
                .onSuccess {
                    userInfoManager.clearParentInfo()
                    appRestarter.restartApp()
                }
                .onFailure {
                    Timber.e("Logout failed $it")
                }
        }
    }

    fun updateIsAlarmChecked(checked: Boolean) {
        _state.update {
            it.copy(
                isAlarmChecked = checked
            )
        }
    }

    // 아이의 연결 유무를 판단하여 ui 업데이트 및 화면 이동
    // Todo : 아이의 연결 여부 처리 확인하기
    fun checkChildCare() {
        viewModelScope.launch {

        }
    }
}
