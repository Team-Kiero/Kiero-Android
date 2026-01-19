package com.kiero.presentation.kid.journey.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.kid.coin.repository.CoinRepository
import com.kiero.data.schedule.model.ScheduleStatus
import com.kiero.data.schedule.repository.ScheduleRepository
import com.kiero.presentation.kid.journey.model.KidJourneyContentUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyHeaderUiModel
import com.kiero.presentation.kid.journey.model.KidJourneyScheduleUiModel
import com.kiero.presentation.kid.journey.model.StoneUiType
import com.kiero.presentation.kid.journey.state.KidJourneySideEffect
import com.kiero.presentation.kid.journey.state.KidJourneyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidJourneyViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val coinRepository: CoinRepository
) : ViewModel() {
    private val coin = coinRepository.myCoin

    private val _state = MutableStateFlow(KidJourneyState.fake())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<KidJourneySideEffect>()
    val sideEffect: SharedFlow<KidJourneySideEffect> = _sideEffect.asSharedFlow()

    fun fetchTodaySchedule() {
        viewModelScope.launch {
            repository.patchScheduleToday()
                .onSuccess { scheduleData ->
                    _state.value = _state.value.copy(
                        header = KidJourneyHeaderUiModel(
                            kidName = coin.value.firstName,
                            currentDate = coin.value.today,
                            coinCount = coin.value.coinAmount,
                            earnedStones = scheduleData.earnedStones,
                            totalScheduleCount = scheduleData.totalSchedule
                        ),
                        content = when (scheduleData.scheduleStatus) {
                            ScheduleStatus.NO_SCHEDULE -> KidJourneyContentUiModel.NoSchedule

                            ScheduleStatus.FIRST_SCHEDULE -> KidJourneyContentUiModel.FirstSchedule(
                                scheduleDetailId = scheduleData.scheduleDetailId,
                                scheduleName = scheduleData.name,
                                stoneType = scheduleData.stoneType?.let { StoneUiType.from(it) },
                                scheduleInfo = KidJourneyScheduleUiModel(
                                    order = scheduleData.scheduleOrder,
                                    startTime = scheduleData.startTime,
                                    endTime = scheduleData.endTime
                                ),
                                isSkippable = scheduleData.isSkippable
                            )

                            ScheduleStatus.NOW_SCHEDULE_EXIST -> KidJourneyContentUiModel.NowSchedule(
                                scheduleDetailId = scheduleData.scheduleDetailId,
                                scheduleName = scheduleData.name,
                                stoneType = scheduleData.stoneType?.let { StoneUiType.from(it) },
                                scheduleInfo = KidJourneyScheduleUiModel(
                                    order = scheduleData.scheduleOrder,
                                    startTime = scheduleData.startTime,
                                    endTime = scheduleData.endTime
                                ),
                                isSkippable = scheduleData.isSkippable
                            )

                            ScheduleStatus.NEXT_SCHEDULE_EXIST -> KidJourneyContentUiModel.NextSchedule(
                                scheduleDetailId = scheduleData.scheduleDetailId,
                                scheduleName = scheduleData.name,
                                stoneType = scheduleData.stoneType?.let { StoneUiType.from(it) },
                                scheduleInfo = KidJourneyScheduleUiModel(
                                    order = scheduleData.scheduleOrder,
                                    startTime = scheduleData.startTime,
                                    endTime = scheduleData.endTime
                                ),
                                isSkippable = scheduleData.isSkippable
                            )

                            ScheduleStatus.FIRE_NOT_LIT -> KidJourneyContentUiModel.FireNotLit(
                                kidName = coin.value.firstName
                            )

                            ScheduleStatus.FIRE_LIT -> KidJourneyContentUiModel.FireLit
                        }
                    )
                    Timber.d("fetchTodaySchedule: $scheduleData")
                }
                .onFailure {
                    Timber.e("fetchTodaySchedule fail: $it")
                }
        }
    }

    fun onNextClick() {
        val content = _state.value.content
        val scheduleDetailId = when (content) {
            is KidJourneyContentUiModel.FirstSchedule -> content.scheduleDetailId
            is KidJourneyContentUiModel.NowSchedule -> content.scheduleDetailId
            is KidJourneyContentUiModel.NextSchedule -> content.scheduleDetailId
            else -> null
        }

        if (scheduleDetailId != null) {
            viewModelScope.launch {
                repository.patchScheduleSkip(scheduleDetailId)
                    .onSuccess {
                        fetchTodaySchedule()
                        Timber.d("patchScheduleSkip: $it")
                    }
                    .onFailure {
                        Timber.d("patchScheduleSkip: $it")
                    }
            }
        }
    }

    fun fetchCoin() {
        viewModelScope.launch {
            coinRepository.getCurrentCoin()
                .onSuccess {
                    Timber.d("fetchCoin: $it")
                }
                .onFailure {
                    Timber.e("fetchCoin fail: $it")
                }
        }
    }
}
