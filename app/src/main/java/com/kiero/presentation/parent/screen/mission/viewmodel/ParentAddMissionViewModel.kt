package com.kiero.presentation.parent.screen.mission.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.mission.model.UpdateMissionModel
import com.kiero.data.parent.mission.repository.ParentMissionAddRepository
import com.kiero.presentation.parent.screen.mission.navigation.MissionEdit
import com.kiero.presentation.parent.screen.mission.state.ParentAddMissionSideEffect
import com.kiero.presentation.parent.screen.mission.state.ParentAddMissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ParentAddMissionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val parentMissionAddRepository: ParentMissionAddRepository,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {

    private val editArgs: MissionEdit? = runCatching {
        savedStateHandle.toRoute<MissionEdit>()
    }.getOrNull()?.takeIf { it.missionId != -1L }

    val isEditMode: Boolean = editArgs != null

    private val _state = MutableStateFlow(ParentAddMissionState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentAddMissionSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val missionNameState = TextFieldState(
        initialText = editArgs?.name.orEmpty()
    )
    val awardTextFieldState = TextFieldState(
        initialText = editArgs?.reward?.takeIf { it > 0 }?.toString() ?: "20"
    )

    init {
        viewModelScope.launch {
            snapshotFlow { awardTextFieldState.text.toString() }.collect { text ->
                val num = text.toIntOrNull()
                if (num != null) {
                    if (num > 500) {
                        awardTextFieldState.edit { replace(0, length, "500") }
                        _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("최대 보상은 500개입니다"))
                    } else if (num == 0) {
                        awardTextFieldState.edit { replace(0, length, "1") }
                    }
                }
            }
        }
    }

    fun onMissionNameMaxLength() {
        viewModelScope.launch {
            _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("미션이 최대 글자수 15자를 초과하였습니다"))
        }
    }

    private val _selectedDate = MutableStateFlow<LocalDate?>(
        if (editArgs != null) {
            editArgs.dueAt
                .takeIf { it.isNotBlank() }
                ?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        } else {
            LocalDate.now()
        }
    )
    val selectedDate = _selectedDate.asStateFlow()

    val displayDate: String
        get() = _selectedDate.value
            ?.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            ?: "마감일을 선택해주세요"

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    private var childId: Long? = null

    fun setChildId() {
        viewModelScope.launch { childId = userInfoManager.getChildIdInfo() }
    }

    fun onDateClick() { _showBottomSheet.update { true } }
    fun onDismissBottomSheet() { _showBottomSheet.update { false } }
    fun onDateSelected(date: LocalDate) {
        _selectedDate.update { date }
        _showBottomSheet.update { false }
    }

    fun onAwardClick(reward: Int) {
        val currentText = awardTextFieldState.text.toString()
        val current = if (currentText.isBlank()) 0 else currentText.toIntOrNull() ?: 0
        val newValue = current + reward

        when {
            newValue > 500 -> {
                awardTextFieldState.edit { replace(0, length, "500") }
                viewModelScope.launch {
                    _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("최대 보상은 500개입니다"))
                }
            }
            newValue < 1 -> {
                awardTextFieldState.edit { replace(0, length, "1") }
            }
            else -> {
                awardTextFieldState.edit { replace(0, length, newValue.toString()) }
            }
        }
    }

    fun onKeypadClosed() {
        val currentText = awardTextFieldState.text.toString()
        val current = currentText.toIntOrNull()

        if (current == null || current < 1) {
            awardTextFieldState.edit { replace(0, length, "1") }
        } else if (current > 500) {
            awardTextFieldState.edit { replace(0, length, "500") }
        }
    }

    fun createMission() {
        onKeypadClosed()
        if (isEditMode) updateMission() else addMission()
    }

    private fun addMission() {
        viewModelScope.launch {
            val name   = missionNameState.text.toString().trim()
            val reward = awardTextFieldState.text.toString().toIntOrNull()
            val dueAt  = _selectedDate.value

            if (!validate(name, reward, dueAt)) return@launch

            val id = childId ?: run {
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("자녀 정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요."))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            parentMissionAddRepository.postParentMission(
                childId = id,
                name    = name,
                reward  = reward!!,
                dueAt   = dueAt!!.toString(),
            ).onSuccess { result ->
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("미션이 추가되었습니다"))
                _sideEffect.emit(ParentAddMissionSideEffect.NavigateToMissionList(result))
            }.onFailure {
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("미션 추가에 실패했습니다"))
                _state.update { s -> s.copy(isLoading = false) }
            }
        }
    }

    private fun updateMission() {
        viewModelScope.launch {
            val missionId = editArgs?.missionId ?: return@launch
            val name      = missionNameState.text.toString().trim()
            val reward    = awardTextFieldState.text.toString().toIntOrNull()
            val dueAt     = _selectedDate.value

            if (!validate(name, reward, dueAt)) return@launch

            _state.update { it.copy(isLoading = true) }

            parentMissionAddRepository.updateMission(
                missionId = missionId,
                request   = UpdateMissionModel(
                    name   = name,
                    reward = reward!!,
                    dueAt  = dueAt!!.toString(),
                ),
            ).onSuccess {
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("미션이 수정되었습니다"))
                _sideEffect.emit(ParentAddMissionSideEffect.NavigateUp)
            }.onFailure {
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("미션 수정에 실패했습니다"))
                _state.update { s -> s.copy(isLoading = false) }
            }
        }
    }

    private suspend fun validate(name: String, reward: Int?, dueAt: LocalDate?): Boolean {
        return when {
            name.isBlank() -> { _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("미션 이름을 입력해주세요")); false }
            reward == null -> { _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("보상 금액을 올바르게 입력해주세요")); false }
            dueAt == null  -> { _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("마감일을 선택해주세요")); false }
            else           -> true
        }
    }
}