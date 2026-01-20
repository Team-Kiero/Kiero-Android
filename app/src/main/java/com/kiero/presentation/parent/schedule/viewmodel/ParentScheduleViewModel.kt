package com.kiero.presentation.parent.schedule.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.model.UiState
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.parent.plan.repository.PlanRepository
import com.kiero.presentation.parent.schedule.plan.state.ParentScheduleState
import com.kiero.presentation.signup.parent.navigation.ParentSignUp
import com.kiero.presentation.signup.parent.state.ParentSignUpSideEffect
import com.kiero.presentation.signup.parent.state.ParentSignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class ParentScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val planRepository: PlanRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<ParentScheduleState>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _authstate = MutableStateFlow(ParentSignUpState())
    val authstate: StateFlow<ParentSignUpState> = _authstate.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentSignUpSideEffect>()
    val sideEffect: SharedFlow<ParentSignUpSideEffect> = _sideEffect.asSharedFlow()

    val info = savedStateHandle.toRoute<ParentSignUp>()


    init {
        fetchSchedule(1L)

        val parentName = info.parentName
        val profileImage = info.parentProfileImage

        initFetchParentInfo(
            parentName = parentName,
            parentProfileImage = profileImage
        )


    }


    fun fetchSchedule(childId: Long) {
        val currentState = (_state.value as? UiState.Success)?.data ?: ParentScheduleState()
        val monday = currentState.currentDate.with(DayOfWeek.MONDAY).toString()
        val sunday = currentState.currentDate.with(DayOfWeek.SUNDAY).toString()

        viewModelScope.launch {

            _state.value = UiState.Success(currentState.copy(isFetching = true))
            planRepository.getPlanAll(childId, monday, sunday)
                .onSuccess { model ->
                    _state.value = UiState.Success(
                        currentState.copy(planAllModel = model)
                    )
                }
                .onFailure {
                    _state.value = UiState.Failure(it.message ?: "데이터 로드 실패")
                }
        }
    }

    fun initFetchParentInfo(
        parentName: String,
        parentProfileImage: String,
    ) {
        viewModelScope.launch {
            _authstate.update { currentState ->
                currentState.copy(
                    parentInfo = currentState.parentInfo.copy(
                        parentName = parentName,
                        parentProfileImage = parentProfileImage
                    )
                )
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.postLogout()

            _sideEffect.emit(ParentSignUpSideEffect.NavigateToSelection)
        }
    }


    fun onDateChange(isNext: Boolean) {
        val currentState = (_state.value as? UiState.Success)?.data ?: return
        val newDate =
            if (isNext) currentState.currentDate.plusWeeks(1) else currentState.currentDate.minusWeeks(
                1
            )
        _state.value = UiState.Success(currentState.copy(currentDate = newDate))
        fetchSchedule(1L)
    }
}

