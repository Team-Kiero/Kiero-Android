package com.kiero.presentation.parent.schedule.mission.auto.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.mission.repository.AutoMissionRepository
import com.kiero.presentation.parent.schedule.mission.auto.model.MissionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AutoMissionViewModel @Inject constructor(
    private val autoMissionRepository: AutoMissionRepository
) : ViewModel() {

    private val _noticeText = MutableStateFlow("")
    val noticeText: StateFlow<String> = _noticeText.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _missions = MutableStateFlow<List<MissionUiModel>>(emptyList())
    val missions: StateFlow<List<MissionUiModel>> = _missions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    private val _shouldNavigateBack = MutableSharedFlow<Unit>()
    val shouldNavigateBack: SharedFlow<Unit> = _shouldNavigateBack.asSharedFlow()

    private val _scrollToPage = MutableSharedFlow<Int>()
    val scrollToPage: SharedFlow<Int> = _scrollToPage.asSharedFlow()

    fun updateNoticeText(text: String) {
        if (text.length > 1000) return
        _noticeText.value = text
    }

    fun analyzeNotice() {
        viewModelScope.launch {
            _isAnalyzing.value = true

            autoMissionRepository.analyzeNotice(_noticeText.value)
                .onSuccess { missions ->
                    _missions.value = missions
                    _currentIndex.value = 0
                }
                .onFailure { e ->
                    val message = when (e) {
                        is TimeoutCancellationException -> "잠시 후 다시 시도해주세요."
                        else -> "알림장 내용을 분석하지 못했어요."
                    }
                    _toastMessage.emit(message)
                }

            _isAnalyzing.value = false
        }
    }

    fun updateMissionName(name: String) {
        val trimmedName = if (name.length > 15) name.substring(0, 15) else name
        updateMissionInList { it.copy(name = trimmedName) }
    }

    fun updateMissionDate(date: LocalDate) {
        updateMissionInList { it.copy(dueAt = date) }
    }

    fun updateMissionReward(reward: Int) {
        viewModelScope.launch {
            val validatedReward = reward.coerceIn(1, 500)
            if (reward > 500) {
                _toastMessage.emit("최대 보상은 500개입니다.")
            }
            updateMissionInList { it.copy(reward = validatedReward) }
        }
    }

    fun updateCurrentIndex(index: Int) {
        _currentIndex.value = index
    }

    fun saveAllMissions(childId: Long) {
        viewModelScope.launch {
            val missions = _missions.value

            val firstErrorIndex = missions.indexOfFirst {
                it.name.isBlank() || it.dueAt.isBefore(LocalDate.now()) || it.reward <= 0
            }

            if (firstErrorIndex != -1) {
                _currentIndex.value = firstErrorIndex
                _scrollToPage.emit(firstErrorIndex)
                _toastMessage.emit(getErrorMessage(missions[firstErrorIndex]))
                return@launch
            }

            _isSaving.value = true

            autoMissionRepository.saveBatchMissions(childId, missions)
                .onSuccess {
                    _toastMessage.emit("미션이 등록되었습니다.")
                    _shouldNavigateBack.emit(Unit)
                }
                .onFailure { e ->
                    val message = when {
                        e.message?.contains("403") == true -> "해당 자녀에 대한 권한이 없습니다."
                        else -> e.message ?: "미션 등록에 실패했습니다."
                    }
                    _toastMessage.emit(message)
                }

            _isSaving.value = false
        }
    }

    fun handleCancel() {
        viewModelScope.launch {
            if (_missions.value.isEmpty()) {
                _shouldNavigateBack.emit(Unit)
            } else {
                _missions.value = emptyList()
                _currentIndex.value = 0
            }
        }
    }

    // 내부 헬퍼 함수
    private fun updateMissionInList(transform: (MissionUiModel) -> MissionUiModel) {
        val currentList = _missions.value.toMutableList()
        val index = _currentIndex.value

        if (index in currentList.indices) {
            currentList[index] = transform(currentList[index])
            _missions.value = currentList
        }
    }

    private fun getErrorMessage(mission: MissionUiModel): String = when {
        mission.name.isBlank() -> "미션 이름을 입력해주세요."
        mission.dueAt.isBefore(LocalDate.now()) -> "마감일은 과거로 설정할 수 없습니다."
        else -> "보상을 입력해주세요."
    }
}