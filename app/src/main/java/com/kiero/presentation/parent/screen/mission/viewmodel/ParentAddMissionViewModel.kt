package com.kiero.presentation.parent.screen.mission.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.analytic.tracker.Tracker
import com.kiero.core.analytic.event.CreationMethod
import com.kiero.core.analytic.event.DueDateType
import com.kiero.core.analytic.event.KieroEvent
import com.kiero.core.common.extension.track
import com.kiero.core.common.state.ClampedNumberFieldState
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.mission.model.UpdateMissionModel
import com.kiero.data.parent.mission.repository.ParentMissionAddRepository
import com.kiero.presentation.parent.screen.mission.component.model.MissionAwardDefaults
import com.kiero.presentation.parent.screen.mission.navigation.MissionEdit
import com.kiero.presentation.parent.screen.mission.state.ParentAddMissionSideEffect
import com.kiero.presentation.parent.screen.mission.state.ParentAddMissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
    private val tracker: Tracker
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
    val awardField = ClampedNumberFieldState(
        min = MissionAwardDefaults.MIN_AWARD,
        max = MissionAwardDefaults.MAX_AWARD,
        initialValue = editArgs?.reward?.takeIf { it > 0 } ?: MissionAwardDefaults.DEFAULT_AWARD,
    )

    init {
        viewModelScope.launch {
            snapshotFlow { awardField.text }.collectLatest { text ->
                val num = text.toIntOrNull()
                if (num != null) {
                    if (num > MissionAwardDefaults.MAX_AWARD) {
                        awardField.setValue(MissionAwardDefaults.MAX_AWARD)
                        _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("최대 보상은 ${MissionAwardDefaults.MAX_AWARD}개입니다"))
                    } else if (num == 0) {
                        awardField.setValue(MissionAwardDefaults.MIN_AWARD)
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

    fun validateAndFixReward() {
        awardField.clampToRange()
    }

    fun onAwardClick(reward: Int) {
        val exceedsMax = awardField.exceedsMaxAfter(reward)
        awardField.applyChange(reward)

        if (exceedsMax) {
            viewModelScope.launch {
                _sideEffect.emit(ParentAddMissionSideEffect.ShowSnackbar("최대 보상은 ${MissionAwardDefaults.MAX_AWARD}개입니다"))
            }
        }
    }


    fun createMission() {
        validateAndFixReward()
        if (isEditMode) updateMission() else addMission()
    }

    private fun getDueDateType(targetDate: LocalDate): DueDateType {
        val today = LocalDate.now()
        return when (targetDate) {
            today -> DueDateType.TODAY
            today.plusDays(1) -> DueDateType.TOMORROW
            else -> DueDateType.FUTURE
        }
    }

    private fun addMission() {
        viewModelScope.launch {
            val name   = missionNameState.text.toString().trim()
            val reward = awardField.value
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
                tracker.track(
                    KieroEvent.Mission.Created(
                        creationMethod = CreationMethod.MANUAL,
                        dueDateType = getDueDateType(dueAt),
                        rewardGold = reward,
                        missionCount = 1,
                        missionId = result.id.toString()
                    )
                )

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
            val reward    = awardField.value
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
