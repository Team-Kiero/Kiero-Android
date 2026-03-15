package com.kiero.presentation.parent.screen.mission.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.model.UiState
import com.kiero.data.parent.mission.repository.MissionRepository
import com.kiero.data.parent.mission.repository.ParentMissionAddRepository
import com.kiero.presentation.kid.mission.model.toUiModel
import com.kiero.presentation.parent.screen.mission.state.ParentMissionSideEffect
import com.kiero.presentation.parent.screen.mission.state.ParentMissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentMissionViewModel @Inject constructor(
    private val missionRepository: MissionRepository,
    private val parentMissionAddRepository: ParentMissionAddRepository,
    private val userInfoManager: UserInfoManager,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<ParentMissionState>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentMissionSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun getMissions() {
        viewModelScope.launch {
            val childId = userInfoManager.getChildIdInfo() ?: run {
                _state.value = UiState.Empty
                return@launch
            }
            missionRepository.getMissions(childId)
                .onSuccess { model ->
                    val uiModel = model.toUiModel()
                    if (uiModel.missionsByDate.isEmpty()) {
                        _state.value = UiState.Empty
                    } else {
                        _state.value = UiState.Success(
                            ParentMissionState(kidMissionByDateList = uiModel)
                        )
                    }
                }
                .onFailure {
                    _state.value = UiState.Failure(it.message ?: "데이터 로드 실패")
                }
        }
    }

    fun deleteMission(missionId: Long) {
        viewModelScope.launch {
            parentMissionAddRepository.deleteMission(missionId)
                .onSuccess {
                    _sideEffect.emit(ParentMissionSideEffect.ShowSnackbar("미션이 삭제되었습니다"))
                    _sideEffect.emit(ParentMissionSideEffect.RefreshMissions)
                }
                .onFailure {
                    _sideEffect.emit(ParentMissionSideEffect.ShowSnackbar("미션 삭제에 실패했습니다"))
                }
        }
    }
}