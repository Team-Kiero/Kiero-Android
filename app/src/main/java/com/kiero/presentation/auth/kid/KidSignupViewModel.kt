package com.kiero.presentation.auth.kid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.presentation.auth.kid.model.toModel
import com.kiero.presentation.auth.kid.state.KidSignUpState
import com.kiero.presentation.auth.kid.state.KidSignupSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class KidSignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(KidSignUpState())
    val state: StateFlow<KidSignUpState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<KidSignupSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onSignupClick() {
        Timber.e("onSignupClick")
        viewModelScope.launch {
            val currentState = _state.value.kidSignUpUiModel
            if (currentState.firstName.text.isBlank() ||
                currentState.lastName.text.isBlank() ||
                currentState.inviteCode.text.isBlank()) {

                _sideEffect.emit(KidSignupSideEffect.ShowSnackbar("이름이나 초대 코드를 확인해줘."))
                return@launch
            }

            postAuthKidLogin()
        }
    }

    fun postAuthKidLogin() {
        viewModelScope.launch {
            val currentState = _state.value.kidSignUpUiModel

            authRepository.postAuthKidLogin(
                request = currentState.toModel()
            ).onSuccess {
                Timber.d("postAuthKidLogin ${it.lastName}")
                _sideEffect.emit(KidSignupSideEffect.NavigateToKidOnboarding)
            }.onFailure {
                viewModelScope.launch {
                    _sideEffect.emit(KidSignupSideEffect.ShowSnackbar(it.message.toString()))
                }
            }
        }
    }
}