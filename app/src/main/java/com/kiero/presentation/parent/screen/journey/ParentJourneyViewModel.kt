package com.kiero.presentation.parent.screen.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.parent.journey.repository.ParentJourneyRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.presentation.parent.screen.journey.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParentJourneyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val parentJourneyRepository: ParentJourneyRepository,
    private val sseManager: SseManager
) : ViewModel() {
    private val _state = MutableStateFlow(ParentJourneyState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentJourneySideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        fetchKidInfo()
        sseManager.startParentSubscription()
    }

    private fun fetchKidInfo() {
        viewModelScope.launch {
            authRepository.getChildren()
                .onSuccess { response ->
                    val kidInfo = response.firstOrNull()?.toUiModel()
                    Timber.e("fetchKidInfo, $kidInfo")
                    if (kidInfo != null) {
                        _state.update { currentState ->
                            currentState.copy(
                                kidInfo = kidInfo
                            )
                        }

                        fetchParentJourney(kidInfo.kidId.toLong())
                    }
                }
                .onFailure {
                    Timber.e("parentJourney ${it.message.toString()}")
                    _sideEffect.emit(ParentJourneySideEffect.ShowSnackbar(message = "아이 정보 불러오기에 실패하였습니다"))
                }
        }
    }

    fun fetchParentJourney(
        childId: Long
    ) {
        viewModelScope.launch {
            parentJourneyRepository.getParentJourney(
                childId = childId
            ).onSuccess { result ->
                _state.update { currentState ->
                    currentState.copy(
                        completeMissions = result.completeMissions.map { it.toUiModel() }.toImmutableList(),
                        incompleteMissions = result.incompleteMissions.map { it.toUiModel() }.toImmutableList(),
                        todayMissionList = result.schedules.mapIndexed { index, it -> it.toUiModel(id = index) }.toImmutableList()
                    )
                }
            }.onFailure {
                Timber.e("parentJourney ${it.message.toString()}")
                _sideEffect.emit(ParentJourneySideEffect.ShowSnackbar(message = "일정 정보 불러오기에 실패하였습니다"))
            }
        }
    }
}
