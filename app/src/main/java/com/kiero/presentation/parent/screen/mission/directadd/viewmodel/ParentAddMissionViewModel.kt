package com.kiero.presentation.parent.screen.mission.directadd.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.formatWithDayOfWeek
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.mission.repository.ParentMissionAddRepository
import com.kiero.presentation.parent.screen.mission.directadd.model.MissionAddValid
import com.kiero.presentation.parent.screen.mission.directadd.model.MissionAwardDefaults
import com.kiero.presentation.parent.screen.mission.directadd.state.ParentAddMissionSideEffect
import com.kiero.presentation.parent.screen.mission.directadd.state.ParentAddMissionState
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
    private val userInfoManager: UserInfoManager,
) : ViewModel() {

    val missionNameState = TextFieldState()
    val awardTextFieldState = TextFieldState(MissionAwardDefaults.DEFAULT_AWARD.toString())

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
        observeAwardInput()
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
                                        MissionAddValid.MAX.message
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

    private fun observeAwardInput() {
        viewModelScope.launch {
            snapshotFlow { awardTextFieldState.text.toString() }
                .collect { text ->
                    val parsed = text.toIntOrNull()
                    if (parsed != null) {
                        if (parsed > MissionAwardDefaults.MAX_AWARD) {
                            _sideEffect.emit(
                                ParentAddMissionSideEffect.ShowSnackbar(
                                    MissionAddValid.MAX.message
                                )
                            )
                        }
                        _currentAwardValue.value = parsed
                        _state.update { it.copy(reward = parsed) }
                    } else if (text.isEmpty()) {
                        _currentAwardValue.value = 0
                        _state.update { it.copy(reward = 0) }
                    }
                }
        }
    }

    private fun observeCurrentAwardValueChanges() {
        viewModelScope.launch {
            _currentAwardValue.collect { value ->
                val currentText = awardTextFieldState.text.toString()
                val currentInt = currentText.toIntOrNull()

                if (value != 0 && currentInt != value) {
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
        val current = _currentAwardValue.value
        val nextReward = (current + change).coerceIn(
            MissionAwardDefaults.MIN_AWARD,
            MissionAwardDefaults.MAX_AWARD
        )

        if (nextReward != current) {
            awardTextFieldState.setTextAndPlaceCursorAtEnd(nextReward.toString())
        } else {
            val message =
                if (nextReward >= MissionAwardDefaults.MAX_AWARD) "최대 보상은 500개입니다." else "최소 보상은 1개입니다."
            viewModelScope.launch {
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar(message))
            }
        }
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

        _state.update { it.copy(selectedDate = formattedDate) }
        onDismissBottomSheet()
    }

    private var isProcessing = false

    fun createMission() {
        if (isProcessing) return
        isProcessing = true

        val currentState = _state.value
        val awardValue = _currentAwardValue.value
        val name = missionNameState.text.toString().trim()

        val validationError = when {
            name.isBlank() -> MissionAddValid.MISSION
            awardValue <= 0 -> MissionAddValid.AWARD
            awardValue > 500 -> MissionAddValid.MAX
            else -> null
        }

        if (validationError != null) {
            isProcessing = false
            viewModelScope.launch {
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar(validationError.message))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val childId = currentState.childId ?: 1L
            val dueAt = currentState.selectedDate ?: LocalDate.now().toString()

            repository.postParentMission(
                childId = childId,
                name = name,
                reward = currentState.reward,
                dueAt = dueAt
            ).onSuccess { mission ->
                _state.update { it.copy(isLoading = false) }
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("미션이 등록되었습니다."))
                _sideEffect.emit(ParentAddMissionSideEffect.NavigateToMissionList(mission))
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false) }
                isProcessing = false
                _sideEffect.emit(
                    ParentAddMissionSideEffect.ShowSnackbar(error.message ?: "미션 생성에 실패했습니다.")
                )
            }
        }
    }
}