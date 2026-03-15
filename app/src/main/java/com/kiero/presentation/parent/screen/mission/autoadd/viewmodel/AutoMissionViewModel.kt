package com.kiero.presentation.parent.screen.mission.autoadd.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.escapeForJson
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.mission.repository.AutoMissionRepository
import com.kiero.presentation.parent.screen.mission.autoadd.model.AutoMissionAwardDefaults
import com.kiero.presentation.parent.screen.mission.autoadd.model.errorMessage
import com.kiero.presentation.parent.screen.mission.autoadd.model.toDomainModel
import com.kiero.presentation.parent.screen.mission.autoadd.model.toUiModels
import com.kiero.presentation.parent.screen.mission.autoadd.model.updateAt
import com.kiero.presentation.parent.screen.mission.autoadd.state.AutoMissionSideEffect
import com.kiero.presentation.parent.screen.mission.autoadd.state.AutoMissionState
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
    val awardTextFieldState = TextFieldState(AutoMissionAwardDefaults.DEFAULT_AWARD.toString())

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
            val escapedText = _state.value.noticeText.escapeForJson()

            autoMissionRepository.analyzeNotice(escapedText)
                .onSuccess { domainData ->
                    val uiMissions = domainData.toUiModels()
                    if (uiMissions.isEmpty()) {
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
                    _sideEffect.emit(AutoMissionSideEffect.ShowToast(message))
                    _state.update { it.copy(isAnalyzing = false) }
                }
        }
    }

    fun updateMissionName(name: String) {
        val trimmedName = if (name.length > 15) name.substring(0, 15) else name
        _state.update { state ->
            state.copy(missions = state.missions.updateAt(state.currentIndex) {
                it.copy(name = trimmedName)
            })
        }
    }

    fun updateMissionDate(date: LocalDate) {
        _state.update { state ->
            state.copy(
                missions = state.missions.updateAt(state.currentIndex) {
                    it.copy(dueAt = date)
                },
                selectedDate = date
            )
        }
    }

    fun onAwardClick(change: Int) {
        val currentState = _state.value
        val currentReward = currentState.currentReward
        val newReward = AutoMissionAwardDefaults.applyChange(currentReward, change)

        viewModelScope.launch {
            when {
                currentReward + change < AutoMissionAwardDefaults.MIN_AWARD -> {
                    _sideEffect.emit(
                        AutoMissionSideEffect.ShowToast(
                            "최소 보상은 ${AutoMissionAwardDefaults.MIN_AWARD}개입니다."
                        )
                    )
                }

                currentReward + change > AutoMissionAwardDefaults.MAX_AWARD -> {
                    _sideEffect.emit(
                        AutoMissionSideEffect.ShowToast(
                            "최대 보상은 ${AutoMissionAwardDefaults.MAX_AWARD}개입니다."
                        )
                    )
                }
            }
        }

        _state.update { state ->
            state.copy(missions = state.missions.updateAt(state.currentIndex) {
                it.copy(reward = newReward)
            })
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
                        missions[firstErrorIndex].errorMessage() ?: return@launch
                    )
                )
                return@launch
            }

            _state.update { it.copy(isSaving = true) }
            val domainMissions = missions.map { it.toDomainModel() }

            autoMissionRepository.saveBatchMissions(childId, domainMissions)
                .onSuccess {
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
                        value > AutoMissionAwardDefaults.MAX_AWARD -> {
                            awardTextFieldState.edit { replace(0, length, AutoMissionAwardDefaults.MAX_AWARD.toString()) }
                            _sideEffect.emit(AutoMissionSideEffect.ShowToast("최대 보상은 ${AutoMissionAwardDefaults.MAX_AWARD}개입니다."))
                            updateMissionReward(AutoMissionAwardDefaults.MAX_AWARD)
                        }

                        value < AutoMissionAwardDefaults.MIN_AWARD -> {
                            awardTextFieldState.edit { replace(0, length, AutoMissionAwardDefaults.MIN_AWARD.toString()) }
                            updateMissionReward(AutoMissionAwardDefaults.MIN_AWARD)
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
            state.copy(missions = state.missions.updateAt(state.currentIndex) {
                it.copy(reward = value)
            })
        }
    }
}