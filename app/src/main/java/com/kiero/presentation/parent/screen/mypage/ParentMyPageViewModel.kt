package com.kiero.presentation.parent.screen.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.app.AppRestarter
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.parent.ParentInfo
import com.kiero.data.auth.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

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
            val userInfo = userInfoManager.getParentInfo()
            _state.update {
                it.copy(
                    parentInfo = userInfo ?: ParentInfo()
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
}
