package com.kiero.presentation.parent.schedule.mission.auto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionContract
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionEvent
import com.kiero.presentation.parent.schedule.mission.auto.state.AutoMissionSideEffect
import com.kiero.presentation.parent.schedule.mission.model.MissionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalDate
import javax.inject.Inject

/**
 * [UI Layer - ViewModel]
 *
 * 역할:
 * 1. UI 상태(State) 관리
 * 2. UI 이벤트(Event) 처리
 * 3. SideEffect 발생 (Toast, Navigation)
 *
 * 현재 상태: FAKE 데이터로 UI 테스트 가능
 *
 * TODO: Repository 주입 후 실제 로직으로 교체
 * @Inject constructor(private val repository: MissionRepository)
 */
@HiltViewModel
class AutoMissionViewModel @Inject constructor(
    // TODO: Repository 주입
    // private val repository: MissionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AutoMissionContract())
    val state: StateFlow<AutoMissionContract> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AutoMissionSideEffect>()
    val sideEffect: SharedFlow<AutoMissionSideEffect> = _sideEffect.asSharedFlow()

    /**
     * 단일 이벤트 입구 (MVI 패턴)
     */
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

    // ==================== SCH-006: 입력 ====================

    /**
     * 알림장 텍스트 입력
     *
     * 제한: 최대 1000자
     */
    private fun updateNoticeText(text: String) {
        if (text.length > 1000) return
        _state.update { it.copy(noticeText = text) }
    }

    /**
     * [SCH-007] 알림장 분석
     *
     * 현재: Contract.FAKE 데이터 사용
     *
     * TODO: 실제 구현
     * ```kotlin
     * private fun analyzeNotice() {
     *     viewModelScope.launch {
     *         _state.update { it.copy(isAnalyzing = true) }
     *
     *         // Repository가 15초 타임아웃 처리
     *         val result = repository.analyzeNotice(_state.value.noticeText)
     *
     *         _state.update { it.copy(isAnalyzing = false) }
     *
     *         when {
     *             result.isSuccess -> {
     *                 _state.update {
     *                     it.copy(
     *                         missions = result.getOrThrow().toImmutableList(),
     *                         currentIndex = 0
     *                     )
     *                 }
     *             }
     *             result.isFailure -> {
     *                 val message = when (result.exceptionOrNull()) {
     *                     is TimeoutException -> "잠시 후 다시 시도해주세요."
     *                     is NetworkException -> "네트워크 연결을 확인해주세요."
     *                     else -> "알림장 내용을 분석하지 못했어요."
     *                 }
     *                 postSideEffect(AutoMissionSideEffect.ShowToast(message))
     *             }
     *         }
     *     }
     * }
     * ```
     */
    private fun analyzeNotice() {
        viewModelScope.launch {
            _state.update { it.copy(isAnalyzing = true) }

            // ===== FAKE 데이터 시뮬레이션 =====
            val result = withTimeoutOrNull(15000L) {
                delay(2000)  // 네트워크 지연 시뮬레이션
                Result.success(AutoMissionContract.FAKE.missions)
            }
            // =================================

            _state.update { it.copy(isAnalyzing = false) }

            when {
                result == null -> {
                    postSideEffect(AutoMissionSideEffect.ShowToast("잠시 후 다시 시도해주세요."))
                }
                result.isSuccess -> {
                    _state.update {
                        it.copy(
                            missions = result.getOrThrow(),
                            currentIndex = 0
                        )
                    }
                }
                result.isFailure -> {
                    postSideEffect(AutoMissionSideEffect.ShowToast("알림장 내용을 분석하지 못했어요."))
                }
            }
        }
    }

    // ==================== SCH-008: 결과 수정 ====================

    /**
     * 미션 이름 수정
     *
     * 제한: 최대 15자
     */
    private fun updateCurrentMissionName(name: String) {
        val trimmedName = if (name.length > 15) name.substring(0, 15) else name
        updateMissionInList { it.copy(name = trimmedName) }
    }

    /**
     * 미션 날짜 수정
     */
    private fun updateCurrentMissionDate(date: LocalDate) {
        updateMissionInList { it.copy(dueAt = date) }
    }

    /**
     * 미션 보상 수정
     *
     * 제한: 1~500개
     */
    private fun updateCurrentMissionReward(reward: Int) {
        val validatedReward = reward.coerceIn(1, 500)

        if (reward > 500) {
            postSideEffect(AutoMissionSideEffect.ShowToast("최대 보상은 500개입니다."))
        }

        updateMissionInList { it.copy(reward = validatedReward) }
    }

    /**
     * 현재 인덱스의 미션만 업데이트하는 유틸리티
     */
    private fun updateMissionInList(transform: (MissionUiModel) -> MissionUiModel) {
        val currentList = _state.value.missions.toMutableList()
        val index = _state.value.currentIndex

        if (index in currentList.indices) {
            currentList[index] = transform(currentList[index])
            _state.update { it.copy(missions = currentList.toImmutableList()) }
        }
    }

    // ==================== SCH-008: 저장 ====================

    /**
     * [SCH-008] 일괄 저장
     *
     * 처리:
     * 1. UI 레벨 유효성 검사
     * 2. Repository 호출
     * 3. 성공 시 메인 화면 복귀
     *
     * 현재: FAKE 성공 처리
     *
     * TODO: 실제 구현
     * ```kotlin
     * private fun saveAllMissions() {
     *     viewModelScope.launch {
     *         // 유효성 검사 (동일)
     *         val firstErrorIndex = ...
     *         if (firstErrorIndex != -1) { return@launch }
     *
     *         _state.update { it.copy(isSaving = true) }
     *
     *         val result = repository.saveBatchMissions(
     *             childId = 1L,  // TODO: 실제 childId
     *             missions = _state.value.missions
     *         )
     *
     *         _state.update { it.copy(isSaving = false) }
     *
     *         when {
     *             result.isSuccess -> {
     *                 postSideEffect(ShowToast("미션이 등록되었습니다."))
     *                 postSideEffect(NavigateToMain)
     *             }
     *             result.isFailure -> {
     *                 val message = when (result.exceptionOrNull()) {
     *                     is PermissionException -> "해당 자녀에 대한 권한이 없습니다."
     *                     else -> "저장에 실패했습니다."
     *                 }
     *                 postSideEffect(ShowToast(message))
     *             }
     *         }
     *     }
     * }
     * ```
     */
    private fun saveAllMissions() {
        viewModelScope.launch {
            val currentMissions = _state.value.missions

            // UI 레벨 유효성 검사
            val firstErrorIndex = currentMissions.indexOfFirst { mission ->
                mission.name.isBlank() ||
                        mission.dueAt.isBefore(LocalDate.now()) ||
                        mission.reward <= 0
            }

            if (firstErrorIndex != -1) {
                _state.update { it.copy(currentIndex = firstErrorIndex) }
                postSideEffect(AutoMissionSideEffect.ScrollToPage(firstErrorIndex))

                val errorMission = currentMissions[firstErrorIndex]
                val message = when {
                    errorMission.name.isBlank() -> "미션 이름을 입력해주세요."
                    errorMission.dueAt.isBefore(LocalDate.now()) -> "마감일은 과거로 설정할 수 없습니다."
                    else -> "보상을 입력해주세요."
                }
                postSideEffect(AutoMissionSideEffect.ShowToast(message))
                return@launch
            }

            // ===== FAKE 저장 시뮬레이션 =====
            _state.update { it.copy(isSaving = true) }
            delay(1000)
            _state.update { it.copy(isSaving = false) }

            postSideEffect(AutoMissionSideEffect.ShowToast("미션이 등록되었습니다."))
            postSideEffect(AutoMissionSideEffect.NavigateToMain)
            // ===============================
        }
    }

    // ==================== 취소 ====================

    /**
     * 취소 버튼 처리
     *
     * - SCH-006 (입력 화면): 메인으로
     * - SCH-008 (결과 화면): 입력 화면으로 (데이터 보존)
     */
    private fun handleCancel() {
        if (_state.value.missions.isEmpty()) {
            postSideEffect(AutoMissionSideEffect.NavigateToMain)
        } else {
            _state.update { it.copy(missions = emptyList<MissionUiModel>().toImmutableList()) }
        }
    }

    // ==================== 유틸리티 ====================

    private fun updateCurrentIndex(index: Int) {
        _state.update { it.copy(currentIndex = index) }
    }

    private fun postSideEffect(effect: AutoMissionSideEffect) {
        viewModelScope.launch { _sideEffect.emit(effect) }
    }
}