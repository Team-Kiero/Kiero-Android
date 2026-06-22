package com.kiero.presentation.parent.screen.mission.auto.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.mission.model.SuggestedMissionModel
import com.kiero.data.parent.mission.repository.AutoMissionRepository
import com.kiero.presentation.parent.screen.mission.auto.model.MissionUiModel
import com.kiero.presentation.parent.screen.mission.auto.state.AutoMissionSideEffect
import com.kiero.presentation.parent.screen.mission.auto.state.AutoMissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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

    private fun observeAwardTextFieldChanges() {
        viewModelScope.launch {
            snapshotFlow { awardTextFieldState.text.toString() }
                .collectLatest{ text ->
                    val num = text.toIntOrNull()

                    if (num != null) {
                        if (num > 500) {
                            awardTextFieldState.setTextAndPlaceCursorAtEnd("500")
                            _sideEffect.emit(AutoMissionSideEffect.ShowToast("최대 보상은 500개입니다."))
                            updateMissionReward(500)
                        } else if (num == 0) {
                            awardTextFieldState.setTextAndPlaceCursorAtEnd("1")
                            updateMissionReward(1)
                        } else {
                            updateMissionReward(num)
                        }
                    } else {
                        if (text.isEmpty()) {
                            updateMissionReward(0)
                        } else {
                            awardTextFieldState.setTextAndPlaceCursorAtEnd("1")
                            updateMissionReward(1)
                        }
                    }
                }
        }
    }
    private fun updateMissionReward(value: Int) {
        _state.update { state ->
            val currentList = state.missions.toMutableList()
            val index = state.currentIndex

            if (index in currentList.indices) {
                currentList[index] = currentList[index].copy(reward = value)
            }

            state.copy(missions = currentList.toImmutableList())
        }
    }

    fun validateAndFixReward() {
        val currentText = awardTextFieldState.text.toString()
        val current = currentText.toIntOrNull()

        if (current == null || current < 1) {
            awardTextFieldState.setTextAndPlaceCursorAtEnd("1")
            updateMissionReward(1)
        } else if (current > 500) {
            awardTextFieldState.setTextAndPlaceCursorAtEnd("500")
            updateMissionReward(500)
        }
    }

    fun onAwardClick(change: Int) {
        val currentText = awardTextFieldState.text.toString()
        val current = if (currentText.isBlank()) 0 else currentText.toIntOrNull() ?: 0
        val newValue = current + change

        when {
            newValue > 500 -> {
                awardTextFieldState.setTextAndPlaceCursorAtEnd("500")
                viewModelScope.launch {
                    _sideEffect.emit(AutoMissionSideEffect.ShowToast("최대 보상은 500개입니다."))
                }
                updateMissionReward(500)
            }
            newValue < 1 -> {
                awardTextFieldState.setTextAndPlaceCursorAtEnd("1")
                updateMissionReward(1)
            }
            else -> {
                awardTextFieldState.setTextAndPlaceCursorAtEnd(newValue.toString())
                updateMissionReward(newValue)
            }
        }
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
                                missions = persistentListOf(),
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
                                missions = uiMissions.toImmutableList(),
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

        _state.update { state ->
            val currentList = state.missions.toMutableList()
            val index = state.currentIndex
            if (index in currentList.indices) {
                currentList[index] = currentList[index].copy(name = trimmedName)
            }
            state.copy(missions = currentList.toImmutableList())
        }
    }

    fun updateMissionDate(date: LocalDate) {
        _state.update { state ->
            val currentList = state.missions.toMutableList()
            val index = state.currentIndex
            if (index in currentList.indices) {
                currentList[index] = currentList[index].copy(dueAt = date)
            }
            state.copy(
                missions = currentList.toImmutableList(),
                selectedDate = date,
                showBottomSheet = false
            )
        }
    }

    fun updateCurrentIndex(index: Int) {
        validateAndFixReward()

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
            awardTextFieldState.setTextAndPlaceCursorAtEnd(mission.reward.toString())
        }
    }

    fun saveAllMissions() {
        validateAndFixReward()

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
                missions = persistentListOf(),
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

    private fun getErrorMessage(mission: MissionUiModel): String = when {
        mission.name.isBlank() -> "미션 이름을 입력해주세요."
        mission.dueAt.isBefore(LocalDate.now()) -> "마감일은 과거로 설정할 수 없습니다."
        else -> "보상을 입력해주세요."
    }
}
