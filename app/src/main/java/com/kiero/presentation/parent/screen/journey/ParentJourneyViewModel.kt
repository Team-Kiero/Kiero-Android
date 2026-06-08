package com.kiero.presentation.parent.screen.journey

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.localstorage.permission.PermissionInfoManager
import com.kiero.core.permission.model.PermissionType
import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.fcm.repository.FcmRepository
import com.kiero.data.parent.journey.repository.ParentJourneyRepository
import com.kiero.data.sse.manager.SseManager
import com.kiero.data.sse.model.parent.SseScheduleEventType
import com.kiero.presentation.parent.screen.journey.model.KidInfo
import com.kiero.presentation.parent.screen.journey.model.TodayJourneyUiModel
import com.kiero.presentation.parent.screen.journey.model.TodayStatus
import com.kiero.presentation.parent.screen.journey.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ParentJourneyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val parentJourneyRepository: ParentJourneyRepository,
    private val sseManager: SseManager,
    private val fcmRepository: FcmRepository,
    private val permissionInfoManager: PermissionInfoManager,
    private val userInfoManager: UserInfoManager
) : ViewModel() {
    private val _state = MutableStateFlow(ParentJourneyState())
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ParentJourneySideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    val notificationDeniedCount = permissionInfoManager.deniedCount(PermissionType.POST_NOTIFICATIONS)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    private var hasShownInitialPrompt = false
    private var hasStartedSseCollectors = false
    private var isParentJourneyFetching = false
    private var lastParentJourneyFetchAt = 0L

    init {
        fetchKidInfo()
        sseManager.startParentSubscription()
    }

    fun checkShouldShowPushPrompt(hasOsPermission: Boolean, deniedCount: Int): Boolean {
        if (hasShownInitialPrompt) return false

        if (!hasOsPermission && deniedCount == 0) {
            hasShownInitialPrompt = true
            return true
        }
        return false
    }

    fun updatePushSetting(isEnabled: Boolean) {
        viewModelScope.launch {
            fcmRepository.updatePushSetting(isEnabled)
                .onFailure { Timber.e("푸시 알림 서버 업데이트 실패: $it") }
        }
    }

    fun increaseDeniedCount(type: PermissionType) {
        viewModelScope.launch {
            permissionInfoManager.increaseDeniedCount(type)
        }
    }

    fun collectConnectionEvents() {
        if (hasStartedSseCollectors) return

        hasStartedSseCollectors = true

        viewModelScope.launch {
            sseManager.connectionState.collect { isConnected ->
                if (isConnected) {
                    val childId = _state.value.kidInfo.kidId.toLong()

                    fetchParentJourney(childId)
                }
            }
        }
    }

    fun collectParentJourneyScheduleEvents() {
        viewModelScope.launch {
            sseManager.parentScheduleEvents.collect { event ->
                val childId = _state.value.kidInfo.kidId.toLong()

                when (event.data.scheduleEventType) {
                    SseScheduleEventType.SCHEDULE_STATUS_UPDATED, SseScheduleEventType.TODAY_MISSION_COMPLETED -> {
                        fetchParentJourney(childId)
                    }
                    SseScheduleEventType.FIRE_LIT -> {
                        _state.update { it.copy(isFireLitToday = true) }
                        fetchParentJourney(childId)
                    }
                    SseScheduleEventType.UNKNOWN -> {
                        Timber.w("알 수 없는 schedule 이벤트: ${event.data.eventType}")
                    }
                }
            }
        }
    }

    fun fetchKidInfo() {
        viewModelScope.launch {
            val localChildId = userInfoManager.getChildIdInfo()
            val localChildFirstName = userInfoManager.getChildFirstName().orEmpty()

            if (localChildId != null) {
                _state.update { currentState ->
                    currentState.copy(
                        kidInfo = KidInfo(
                            kidId = localChildId.toString(),
                            kidName = localChildFirstName
                        )
                    )
                }

                fetchParentJourney(localChildId)
                collectParentJourneyScheduleEvents()
                collectConnectionEvents()
                return@launch
            }

            authRepository.getChildren()
                .onSuccess { response ->
                    val kidInfo = response.firstOrNull()?.toUiModel()
                    Timber.e("fetchKidInfo, $kidInfo")
                    if (kidInfo != null) {
                        userInfoManager.saveChildIdInfo(kidInfo.kidId.toLong())
                        userInfoManager.saveChildName(
                            lastName = response.first().childLastName,
                            firstName = response.first().childFirstName
                        )
                        _state.update { currentState ->
                            currentState.copy(
                                kidInfo = kidInfo
                            )
                        }

                        collectParentJourneyScheduleEvents()
                        collectConnectionEvents()
                        fetchParentJourney(kidInfo.kidId.toLong())
                    }
                }
                .onFailure {
                    Timber.e("parentJourney ${it.message.toString()}")
                    _sideEffect.emit(ParentJourneySideEffect.ShowSnackbar(message = "아이 정보 불러오기에 실패하였습니다"))
                }
        }
    }

    fun fetchParentJourney(childId: Long) {
        val now = SystemClock.elapsedRealtime()
        if (isParentJourneyFetching || now - lastParentJourneyFetchAt < PARENT_JOURNEY_FETCH_THROTTLE_MS) {
            return
        }

        isParentJourneyFetching = true
        lastParentJourneyFetchAt = now

        viewModelScope.launch {
            try {
                parentJourneyRepository.getParentJourney(
                    childId = childId
                ).onSuccess { result ->
                    val currentTime = LocalTime.now()

                    _state.update { currentState ->
                        currentState.copy(
                            isFireLitToday = result.isFireLitToday,
                            completeMissions = result.completeMissions.map { it.toUiModel() }.toImmutableList(),
                            incompleteMissions = result.incompleteMissions.map { it.toUiModel() }.toImmutableList(),
                            todayMissionList = if (result.isFireLitToday) {
                                result.schedules.toUiModels(currentTime).toPersistentList().add(
                                    TodayJourneyUiModel(todayStatus = TodayStatus.TODAY_COMPLETED)
                                )
                            } else {
                                result.schedules.toUiModels(currentTime).toPersistentList()
                            }
                        )
                    }
                }.onFailure {
                    Timber.e("parentJourney ${it.message.toString()}")
                    _sideEffect.emit(ParentJourneySideEffect.ShowSnackbar(message = "일정 정보 불러오기에 실패하였습니다"))
                }
            } finally {
                isParentJourneyFetching = false
            }
        }
    }

    fun fetchScheduleImage(
        scheduleDetailId: Long
    ) {
        viewModelScope.launch {
            parentJourneyRepository.patchScheduleImage(
                scheduleDetailId = scheduleDetailId
            ).onSuccess { result ->
                _state.update {
                    it.copy(
                        selectedJourneyImageUrl = result.imageUrl
                    )
                }
            }.onFailure {
                Timber.e(it)
                _sideEffect.emit(ParentJourneySideEffect.ShowSnackbar(message = "이미지 정보 불러오기에 실패하였습니다"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sseManager.stopSubscription()
    }

    companion object {
        private const val PARENT_JOURNEY_FETCH_THROTTLE_MS = 500L
    }
}
