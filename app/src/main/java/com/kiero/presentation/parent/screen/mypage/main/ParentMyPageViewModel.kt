package com.kiero.presentation.parent.screen.mypage.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.app.AppRestarter
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.parent.ParentInfo
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.parent.mypage.parent.repository.ParentMyPageRepository
import com.kiero.presentation.parent.screen.mypage.main.model.toUiModel
import com.kiero.presentation.parent.screen.mypage.model.ChildConnectionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
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
    private val parentMyPageRepository: ParentMyPageRepository,
    private val userInfoManager: UserInfoManager,
    private val appRestarter: AppRestarter
) : ViewModel() {
    private val _state = MutableStateFlow(ParentMyPageState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentMyPageSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()


    init {
        fetchMyPageTerms()
        fetchInfo()
    }

    fun fetchMyPageTerms() {
        viewModelScope.launch {
            parentMyPageRepository.getParentTermsExternalLink()
                .onSuccess { result ->
                    _state.update { currentState ->
                        currentState.copy(
                            myPageMenus = result.map { it.toUiModel() }.toImmutableList()
                        )
                    }
                }
                .onFailure {
                    Timber.e(it, "약관 불러오기 실패")
                    _sideEffect.emit(ParentMyPageSideEffect.ShowToast("약관 불러오기에 실패했어요. 다시 시도해주세요."))
                }
        }
    }
    fun fetchInfo() {
        viewModelScope.launch {
            parentMyPageRepository.getParentMyProfile()
                .onSuccess { response ->
                    _state.update {
                        it.copy(
                            parentInfo = ParentInfo(
                                name = response.name,
                                profileImage = response.image ?: ""
                            ),

                            connectionStatus = if (response.hasPendingChildSession) {
                                ChildConnectionStatus.PENDING
                            } else {
                                ChildConnectionStatus.REISSUE
                            },

                            isAlarmChecked = response.pushNotificationEnabled
                        )
                    }
                }
                .onFailure { error ->
                    Timber.e(error, "프로필 정보 불러오기 실패")
                    val kidInfo = userInfoManager.getChildIdInfo()
                    val userInfo = userInfoManager.getParentInfo()

                    _state.update {
                        it.copy(
                            parentInfo = userInfo ?: ParentInfo(),
                            connectionStatus = if (kidInfo != null) ChildConnectionStatus.REISSUE else ChildConnectionStatus.PENDING
                        )
                    }
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
}
