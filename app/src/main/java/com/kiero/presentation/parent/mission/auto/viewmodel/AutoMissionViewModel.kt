package com.kiero.presentation.parent.mission.auto.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.mission.model.SuggestedMissionModel
import com.kiero.data.parent.mission.repository.AutoMissionRepository
import com.kiero.presentation.parent.mission.auto.model.MissionUiModel
import com.kiero.presentation.parent.mission.auto.state.AutoMissionSideEffect
import com.kiero.presentation.parent.mission.auto.state.AutoMissionState
import com.kiero.presentation.parent.mission.component.model.MissionAwardDefaults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AutoMissionViewModel @Inject constructor(
    private val autoMissionRepository: AutoMissionRepository,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {

    private val _state = MutableStateFlow(AutoMissionState())
    val state: StateFlow<AutoMissionState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AutoMissionSideEffect>()
    val sideEffect: SharedFlow<AutoMissionSideEffect> = _sideEffect.asSharedFlow()

    val awardTextFieldState = TextFieldState(initialText = "20")

    init {
        observeAwardTextFieldChanges()
    }

    fun updateNoticeText(text: String) {
        if (text.length > 1000) return
        _state.update { it.copy(noticeText = text) }
    }

    fun analyzeNotice() {
        viewModelScope.launch {
            _state.update { it.copy(isAnalyzing = true) }
            val rawText = _state.value.noticeText

            val escapedText = rawText
                .replace("\"", "\\\"")
                .replace("\n", "\\n")

            autoMissionRepository.analyzeNotice(escapedText)
                .onSuccess { domainData ->
                    if (domainData.suggestedMissions.isEmpty()) {
                        Timber.e("message suggestedMissions")
                        _sideEffect.emit(AutoMissionSideEffect.ShowToast("알림장 내용을 분석하지 못했어요."))
                        delay(2000L)
                        _state.update {
                            it.copy(
                                missions = emptyList(),
                                currentIndex = 0,
                                hasViewedLastPage = false,
                                isAnalyzing = false
                            )
                        }
                    } else {
                        val uiMissions = domainData.suggestedMissions.map { suggested ->
                            MissionUiModel(
                                name = suggested.name,
                                reward = suggested.reward,
                                dueAt = LocalDate.parse(suggested.dueAt),
                                isCompleted = false
                            )
                        }
                        _state.update {
                            it.copy(
                                missions = uiMissions,
                                currentIndex = 0,
                                hasViewedLastPage = uiMissions.size == 1,
                                isAnalyzing = false
                            )
                        }
                    }
                }
                .onFailure { e ->
                    val message = when (e) {
                        is TimeoutCancellationException -> "잠시 후 다시 시도해주세요."
                        else -> "알림장 내용을 분석하지 못했어요."
                    }
                    Timber.e("message $message")
                    _sideEffect.emit(AutoMissionSideEffect.ShowToast(message))
                    _state.update { it.copy(isAnalyzing = false) }
                }
        }
    }

    fun updateMissionName(name: String) {
        val trimmedName = if (name.length > 15) name.substring(0, 15) else name

        _state.update { currentState ->
            val updatedMissions = currentState.missions.toMutableList().apply {
                val index = currentState.currentIndex
                if (index in indices) {
                    this[index] = this[index].copy(name = trimmedName)
                }
            }
            currentState.copy(missions = updatedMissions)
        }
    }

    fun updateMissionDate(date: LocalDate) {
        _state.update { currentState ->

            val updatedMissions = currentState.missions.toMutableList().apply {
                val index = currentState.currentIndex
                if (index in indices) {
                    this[index] = this[index].copy(dueAt = date)
                }
            }

            currentState.copy(
                missions = updatedMissions,
                selectedDate = date,
                showBottomSheet = false
            )
        }
    }

    fun onAwardClick(change: Int) {
        val currentState = _state.value
        val currentReward = currentState.currentReward
        val newReward = MissionAwardDefaults.applyChange(currentReward, change)

        viewModelScope.launch {
            when {
                currentReward + change < MissionAwardDefaults.MIN_AWARD -> {
                    _sideEffect.emit(
                        AutoMissionSideEffect.ShowToast(
                            "최소 보상은 ${MissionAwardDefaults.MIN_AWARD}개입니다."
                        )
                    )
                }

                currentReward + change > MissionAwardDefaults.MAX_AWARD -> {
                    _sideEffect.emit(
                        AutoMissionSideEffect.ShowToast(
                            "최대 보상은 ${MissionAwardDefaults.MAX_AWARD}개입니다."
                        )
                    )
                }
            }
        }

        _state.update { currentState ->
            val updatedMissions = currentState.missions.toMutableList().apply {
                val index = currentState.currentIndex
                if (index in indices) {
                    this[index] = this[index].copy(reward = newReward)
                }
            }
            currentState.copy(missions = updatedMissions)
        }

        awardTextFieldState.edit {
            replace(0, length, newReward.toString())
        }
    }

    fun updateCurrentIndex(index: Int) {
        _state.update { currentState ->
            val newHasViewedLastPage = if (index == currentState.missions.size - 1) {
                true
            } else {
                currentState.hasViewedLastPage
            }

            currentState.copy(
                currentIndex = index,
                hasViewedLastPage = newHasViewedLastPage
            )
        }

        _state.value.currentMission?.let { mission ->
            _state.update { it.copy(selectedDate = mission.dueAt) }
            awardTextFieldState.edit {
                replace(0, length, mission.reward.toString())
            }
        }
    }

    fun saveAllMissions() {
        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: return@launch
            val currentState = _state.value
            val missions = currentState.missions

            val firstErrorIndex = missions.indexOfFirst {
                it.name.isBlank() ||
                        it.dueAt.isBefore(LocalDate.now()) ||
                        it.reward <= 0
            }

            if (firstErrorIndex != -1) {
                _state.update { it.copy(currentIndex = firstErrorIndex) }
                _sideEffect.tryEmit(AutoMissionSideEffect.ScrollToPage(firstErrorIndex))
                _sideEffect.tryEmit(
                    AutoMissionSideEffect.ShowToast(
                        getErrorMessage(missions[firstErrorIndex])
                    )
                )
                return@launch
            }

            _state.update { it.copy(isSaving = true) }
            val domainMissions = missions.map { uiModel ->
                SuggestedMissionModel(
                    name = uiModel.name,
                    reward = uiModel.reward,
                    dueAt = uiModel.dueAt.toString()
                )
            }

            autoMissionRepository.saveBatchMissions(childId, domainMissions)
                .onSuccess {
                    Timber.e("message saveBatchMissions")
                    _state.update {
                        it.copy(
                            hasViewedLastPage = false
                        )
                    }
                    _sideEffect.emit(
                        AutoMissionSideEffect.ShowToastAndNavigate("미션이 등록되었습니다."),
                    )
                }
                .onFailure { e ->
                    val message = when {
                        e.message?.contains("403") == true -> "해당 자녀에 대한 권한이 없습니다."
                        else -> "미션 등록에 실패했습니다."
                    }
                    _sideEffect.emit(AutoMissionSideEffect.ShowToast(message))
                }

            _state.update { it.copy(isSaving = false) }
        }
    }

    fun backToInputScreen() {
        _state.update {
            it.copy(
                missions = emptyList(),
                currentIndex = 0,
                hasViewedLastPage = false
            )
        }
    }

    fun handleCancel() {
        viewModelScope.launch {
            _sideEffect.emit(AutoMissionSideEffect.NavigateBack)
        }
    }

    fun showDatePicker() {
        _state.update { it.copy(showBottomSheet = true) }
    }

    fun dismissDatePicker() {
        _state.update { it.copy(showBottomSheet = false) }
    }

    fun onDateSelected(date: LocalDate) {
        updateMissionDate(date)
    }

    private fun observeAwardTextFieldChanges() {
        viewModelScope.launch {
            snapshotFlow { awardTextFieldState.text.toString() }
                .collect { text ->
                    if (text.isEmpty()) {
                        return@collect
                    }
                    val value = text.toIntOrNull() ?: return@collect
                    val currentState = _state.value

                    when {
                        value > 500 -> {
                            awardTextFieldState.edit {
                                replace(0, length, "500")
                            }
                            _sideEffect.emit(
                                AutoMissionSideEffect.ShowToast("최대 보상은 500개입니다.")
                            )
                            updateMissionReward(500)
                        }

                        value < 1 -> {
                            awardTextFieldState.edit {
                                replace(0, length, "1")
                            }
                            updateMissionReward(1)
                        }

                        value != currentState.currentReward -> {
                            updateMissionReward(value)
                        }
                    }
                }
        }
    }

    private fun updateMissionReward(value: Int) {
        _state.update { state ->
            val updatedMissions = state.missions.toMutableList().apply {
                val index = state.currentIndex
                if (index in indices) {
                    this[index] = this[index].copy(reward = value)
                }
            }
            state.copy(missions = updatedMissions)
        }
    }

    private fun getErrorMessage(mission: MissionUiModel): String = when {
        mission.name.isBlank() -> "미션 이름을 입력해주세요."
        mission.dueAt.isBefore(LocalDate.now()) -> "마감일은 과거로 설정할 수 없습니다."
        else -> "보상을 입력해주세요."
    }
}