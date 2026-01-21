package com.kiero.presentation.parent.schedule.mission.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.formatWithDayOfWeek
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.repository.ParentMissionAddRepository
import com.kiero.presentation.parent.schedule.mission.component.model.MissionAwardDefaults
import com.kiero.presentation.parent.schedule.mission.component.model.ParentMissionAddValid
import com.kiero.presentation.parent.schedule.mission.state.ParentAddMissionSideEffect
import com.kiero.presentation.parent.schedule.mission.state.ParentAddMissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ParentAddMissionViewModel @Inject constructor(
    private val repository: ParentMissionAddRepository,
    private val userInfoManager: UserInfoManager
) : ViewModel() {

    val missionNameState = TextFieldState()
    val awardTextFieldState = TextFieldState()

    private val _state = MutableStateFlow(ParentAddMissionState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentAddMissionSideEffect>()
    val sideEffect: SharedFlow<ParentAddMissionSideEffect> = _sideEffect.asSharedFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    private val _currentAwardValue = MutableStateFlow(MissionAwardDefaults.DEFAULT_AWARD)
    val currentAwardValue = _currentAwardValue.asStateFlow()

    private val _selectedDate = MutableStateFlow<String?>(
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    )
    val selectedDate = _selectedDate.asStateFlow()

    val displayDate: String
        get() = _selectedDate.value?.formatWithDayOfWeek ?: "마감일을 선택해주세요"

    init {
        _state.update {
            it.copy(selectedDate = _selectedDate.value)
        }
        observeAwardTextFieldChanges()
        observeCurrentAwardValueChanges()
        observeMissionNameChanges()
    }

    private fun observeAwardTextFieldChanges() {
        viewModelScope.launch {
            snapshotFlow { awardTextFieldState.text.toString() }
                .collect { text ->
                    if (text.isNotEmpty()) {
                        val parsed = text.toIntOrNull()

                        if (parsed != null) {
                            if (parsed > 500) {
                                _sideEffect.emit(
                                    ParentAddMissionSideEffect.ShowSnackbar(
                                        ParentMissionAddValid.MAX.message
                                    )
                                )

                            }

                            val newValue = MissionAwardDefaults.constrainValue(parsed)
                            if (newValue != _currentAwardValue.value) {
                                _currentAwardValue.value = newValue
                                _state.update { it.copy(reward = newValue) }
                            }
                        }
                    }
                }
        }
    }

    private fun observeCurrentAwardValueChanges() {
        viewModelScope.launch {
            _currentAwardValue.collect { value ->
                if (awardTextFieldState.text.toString().toIntOrNull() != value) {
                    awardTextFieldState.setTextAndPlaceCursorAtEnd(value.toString())
                }
            }
        }
    }

    private fun observeMissionNameChanges() {
        viewModelScope.launch {
            snapshotFlow { missionNameState.text.toString() }
                .collect { name ->
                    _state.update { it.copy(missionName = name) }
                }
        }
    }

    fun onDateClick() {
        _showBottomSheet.value = true
    }

    fun onDismissBottomSheet() {
        _showBottomSheet.value = false
    }

    fun onAwardClick(change: Int) {
        _currentAwardValue.value =
            MissionAwardDefaults.applyChange(_currentAwardValue.value, change)
    }

    fun onDateSelected(date: String) {
        _selectedDate.value = date
        _state.update { it.copy(selectedDate = date) }
        _showBottomSheet.value = false
    }

    fun setChildId() {
        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            _state.update { it.copy(childId = childId) }
        }
    }

    fun onDateSelected(date: LocalDate) {
        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        _selectedDate.value = formattedDate
        onDismissBottomSheet()
    }


    fun createMission() {
        val currentState = _state.value
        val awardValue = _currentAwardValue.value

        val validationError = when {
            currentState.missionName.isBlank() -> ParentMissionAddValid.MISSION
            awardValue <= 0 -> ParentMissionAddValid.AWARD
            awardValue > 500 -> ParentMissionAddValid.MAX
            else -> null
        }

        if (validationError != null) {
            viewModelScope.launch {
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar(validationError.message))
            }
            return
        }

        viewModelScope.launch {
            val childId = currentState.childId ?: 1L
            val name = currentState.missionName
            val reward = currentState.reward
            val dueAt = currentState.selectedDate ?: LocalDate.now().toString() // 만약의 null 대비

            _state.update { it.copy(isLoading = true) }

            repository.postParentMission(
                childId = childId,
                name = name,
                reward = reward,
                dueAt = dueAt
            ).onSuccess { mission ->
                _state.update { it.copy(isLoading = false) }
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("미션이 등록되었습니다."))
                _sideEffect.emit(ParentAddMissionSideEffect.NavigateToMissionList(mission))
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                _sideEffect.emit(
                    ParentAddMissionSideEffect.ShowSnackbar(error.message ?: "미션 생성에 실패했습니다.")
                )
            }
        }
    }
}
