package com.kiero.presentation.kid.journey.camera.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.core.common.extension.updateSuccess
import com.kiero.core.common.util.ImageUriManager
import com.kiero.core.common.util.successData
import com.kiero.core.model.UiState
import com.kiero.domain.kid.schedule.usecase.CompleteScheduleWithImageUseCase
import com.kiero.presentation.kid.journey.camera.navigation.Camera
import com.kiero.presentation.kid.journey.camera.state.KidCameraSideEffect
import com.kiero.presentation.kid.journey.camera.state.KidCameraState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidCameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val completeScheduleWithImageUseCase: CompleteScheduleWithImageUseCase,
    private val imageUriManager: ImageUriManager
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

    fun createTempFile() {
        if (_state.value.successData?.tempUri != null) return

        viewModelScope.launch {
            val uriString = withContext(Dispatchers.IO) {
                imageUriManager.createTempImageUri()
            }

            if (uriString != null) {
                updateTempUri(uriString)
            }
        }
    }

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
            val currentState = _state.value.successData ?: return@launch

            if (currentState.imageUri?.isBlank() == true) {
                Timber.e("이미지 URI가 없습니다.")
                return@launch
            }

            _state.updateSuccess { it.copy(isLoading = true) }

            val timerJob = async {
                delay(4000L)
            }

            val apiJob = async {
                completeScheduleWithImageUseCase(
                    uriString = currentState.imageUri.orEmpty(),
                    fileName = fileName,
                    contentType = contentType,
                    scheduleDetailId = currentState.scheduleDetailId
                )
            }

            try {
                timerJob.await()
                apiJob.await()
                    .onSuccess {
                        Timber.d("이미지 업로드 및 일정 완료 성공")
                        _sideEffect.emit(KidCameraSideEffect.NavigateUp)
                    }
                    .onFailure { e ->
                        Timber.e(e, "이미지 업로드 또는 일정 완료 실패")
                    }
            } catch (e: Exception) {
                Timber.e(e, "예외 발생")
            } finally {
                _state.updateSuccess { it.copy(isLoading = false) }
            }
        }
    }
}