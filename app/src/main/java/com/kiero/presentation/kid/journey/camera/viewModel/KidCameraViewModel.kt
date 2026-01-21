package com.kiero.presentation.kid.journey.camera.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.common.extension.updateSuccess
import com.kiero.core.model.UiState
import com.kiero.data.kid.schedule.repository.ScheduleRepository
import com.kiero.presentation.kid.journey.camera.navigation.Camera
import com.kiero.presentation.kid.journey.camera.state.KidCameraSideEffect
import com.kiero.presentation.kid.journey.camera.state.KidCameraState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidCameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ScheduleRepository
) : ViewModel() {
    private val camera = savedStateHandle.toRoute<Camera>()

    private val _state = MutableStateFlow<UiState<KidCameraState>>(
        UiState.Success(
            KidCameraState(
                scheduleDetailId = camera.scheduleDetailId,
                stoneType = camera.stoneType
            )
        )
    )
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<KidCameraSideEffect>()
    val sideEffect: SharedFlow<KidCameraSideEffect> = _sideEffect.asSharedFlow()

    fun updateTempUri(uri: String) {
        _state.updateSuccess {
            it.copy(tempUri = uri)
        }
    }

    fun updateImageUri() {
        _state.updateSuccess {
            it.copy(imageUri = it.tempUri)
        }
    }

    fun postImage(
        fileName: String,
        contentType: String
    ) {
        viewModelScope.launch {
            val currentState = (_state.value as? UiState.Success)?.data ?: return@launch
            _state.updateSuccess { it.copy(isLoading = true) }

            val timerJob = async {
                delay(4000L)
            }

            val apiJob = async {
                repository.postPresignedUrl(
                    fileName = fileName,
                    contentType = contentType
                ).mapCatching { result ->
                    repository.patchScheduleComplete(
                        scheduleDetailId = currentState.scheduleDetailId,
                        imageUrl = result.presignedUrl.split("?").first()
                    ).getOrThrow()
                }
            }

            val apiResult = apiJob.await()
            timerJob.await()

            apiResult
                .onSuccess {
                    Timber.d("scheduleComplete success")
                    _sideEffect.emit(KidCameraSideEffect.NavigateUp)
                    _state.updateSuccess { it.copy(isLoading = false) }
                }
                .onFailure {
                    Timber.e("postImage or scheduleComplete fail: $it")
                    _state.updateSuccess { it.copy(isLoading = false) }
                }
        }
    }
}