package com.kiero.presentation.parent.schedule.mission.auto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionContract
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionEvent
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionSideEffect
import com.kiero.presentation.parent.schedule.mission.model.MissionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AutoMissionViewModel @Inject constructor(
    // TODO: MissionRepository 주입 필요
) : ViewModel() {

    private val _state = MutableStateFlow(AutoMissionContract())
    val state: StateFlow<AutoMissionContract> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AutoMissionSideEffect>()
    val sideEffect: SharedFlow<AutoMissionSideEffect> = _sideEffect.asSharedFlow()

    // TODO: 이벤트가 늘어날 경우 Reducer나 Processor로 로직을 분리하는 리팩터링 고려
    fun onEvent(event: AutoMissionEvent) {
        when (event) {
            is AutoMissionEvent.OnNoticeTextChanged -> updateNoticeText(event.text)
            is AutoMissionEvent.OnAnalyzeClick -> analyzeNotice()
            is AutoMissionEvent.OnPageChanged -> updateCurrentIndex(event.index)
            is AutoMissionEvent.UpdateMissionName -> updateCurrentMissionName(event.name)
            is AutoMissionEvent.UpdateMissionDate -> updateCurrentMissionDate(event.date)
            is AutoMissionEvent.UpdateMissionReward -> updateCurrentMissionReward(event.reward)
            is AutoMissionEvent.OnSaveAllClick -> saveAllMissions()
            is AutoMissionEvent.OnCancelClick -> handleCancel()
        }
    }

    // 알림장 입력 로직
    private fun updateNoticeText(text: String) {
        if (text.length > 1000) return
        _state.update { it.copy(noticeText = text) }
    }

    // 알림장 분석 로직
    private fun analyzeNotice() {
        viewModelScope.launch {
            _state.update { it.copy(isAnalyzing = true) }

            // TODO: Repository.analyzeNotice() 연동 및 15초 타임아웃 처리
            val result = withTimeoutOrNull(15000L) {
                delay(2000) // 분석 시뮬레이션
                Result.success(AutoMissionContract.FAKE.missions)
            }

            _state.update { it.copy(isAnalyzing = false) }

            when {
                result == null -> postSideEffect(AutoMissionSideEffect.ShowToast("잠시 후 다시 시도해주세요."))
                result.isSuccess -> {
                    _state.update {
                        it.copy(missions = result.getOrThrow(), currentIndex = 0)
                    }
                }
                result.isFailure -> postSideEffect(AutoMissionSideEffect.ShowToast("알림장 내용을 분석하지 못했어요."))
            }
        }
    }

    // 결과 수정 로직

    private fun updateCurrentMissionName(name: String) {
        val trimmedName = if (name.length > 15) name.substring(0, 15) else name
        updateMissionInList { it.copy(name = trimmedName) }
    }

    private fun updateCurrentMissionDate(date: LocalDate) {
        updateMissionInList { it.copy(dueAt = date) }
    }

    private fun updateCurrentMissionReward(reward: Int) {
        val validatedReward = reward.coerceIn(1, 500)
        if (reward > 500) postSideEffect(AutoMissionSideEffect.ShowToast("최대 보상은 500개입니다."))
        updateMissionInList { it.copy(reward = validatedReward) }
    }

    private fun updateMissionInList(transform: (MissionUiModel) -> MissionUiModel) {
        val currentList = _state.value.missions.toMutableList()
        val index = _state.value.currentIndex

        if (index in currentList.indices) {
            currentList[index] = transform(currentList[index])
            _state.update { it.copy(missions = currentList.toImmutableList()) }
        }
    }

    // 일괄 저장 및 유효성 검증

    private fun saveAllMissions() {
        viewModelScope.launch {
            val missions = _state.value.missions

            // 1. 유효성 검사: 실패 시 해당 페이지로 이동 및 토스트 발생
            val firstErrorIndex = missions.indexOfFirst {
                it.name.isBlank() || it.dueAt.isBefore(LocalDate.now()) || it.reward <= 0
            }

            if (firstErrorIndex != -1) {
                _state.update { it.copy(currentIndex = firstErrorIndex) }
                postSideEffect(AutoMissionSideEffect.ScrollToPage(firstErrorIndex))
                postSideEffect(AutoMissionSideEffect.ShowToast(getErrorMessage(missions[firstErrorIndex])))
                return@launch
            }

            // 2. TODO: Repository.saveBatchMissions() 연동
            _state.update { it.copy(isSaving = true) }
            delay(1000) // 저장 시뮬레이션
            _state.update { it.copy(isSaving = false) }

            postSideEffect(AutoMissionSideEffect.ShowToast("미션이 등록되었습니다."))
            postSideEffect(AutoMissionSideEffect.NavigateToMain)
        }
    }

    private fun getErrorMessage(mission: MissionUiModel): String = when {
        mission.name.isBlank() -> "미션 이름을 입력해주세요."
        mission.dueAt.isBefore(LocalDate.now()) -> "마감일은 과거로 설정할 수 없습니다."
        else -> "보상을 입력해주세요."
    }

    // 유틸리티 로직
    private fun handleCancel() {
        if (_state.value.missions.isEmpty()) {
            _state.update { AutoMissionContract() }
            postSideEffect(AutoMissionSideEffect.NavigateToMain)
        } else {
            _state.update {
                it.copy(
                    missions = persistentListOf(),
                    currentIndex = 0
                )
            }
        }
    }

    private fun updateCurrentIndex(index: Int) {
        _state.update { it.copy(currentIndex = index) }
    }

    private fun postSideEffect(effect: AutoMissionSideEffect) {
        viewModelScope.launch { _sideEffect.emit(effect) }
    }
}