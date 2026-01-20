package com.kiero.presentation.kid.journey.camera.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kiero.data.kid.schedule.repository.ScheduleRepository
import com.kiero.presentation.kid.journey.camera.navigation.Camera
import com.kiero.presentation.kid.journey.camera.state.KidCameraSideEffect
import com.kiero.presentation.kid.journey.camera.state.KidCameraState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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

    private val _state = MutableStateFlow(
        KidCameraState(
            scheduleDetailId = camera.scheduleDetailId,
            stoneType = camera.stoneType
        )
    )
    val state = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<KidCameraSideEffect>()
    val sideEffect: SharedFlow<KidCameraSideEffect> = _sideEffect.asSharedFlow()

    fun postImage(
        fileName: String,
        contentType: String
    ) {
        viewModelScope.launch {
            repository.postPresignedUrl(
                fileName = fileName,
                contentType = contentType
            ).onSuccess { result ->
                Timber.e("postImage success: $result")
                val deferred = async {
                    repository.patchScheduleComplete(
                        scheduleDetailId = state.value.scheduleDetailId,
                        imageUrl = result.presignedUrl.split("?").first()
                    )
                }

                val scheduleComplete = deferred.await()

                scheduleComplete.onSuccess {
                    Timber.e("deferred success: $it")
                    _sideEffect.emit(KidCameraSideEffect.NavigateUp)
                }.onFailure {
                    Timber.e("deferred fail: $it")
                }
            }.onFailure {
                Timber.e("postImage fail: $it")
            }
        }
    }

    fun patchScheduleComplete() {
        viewModelScope.launch {

        }
    }

}