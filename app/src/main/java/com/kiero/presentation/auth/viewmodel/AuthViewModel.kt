package com.kiero.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.util.handleError
import com.kiero.core.model.UiState
import com.kiero.data.auth.repository.DummyRepository
import com.kiero.presentation.auth.model.DummySideEffect
import com.kiero.presentation.auth.model.DummyState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dummyRepository: DummyRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(DummyState())
    val state: StateFlow<DummyState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<DummySideEffect>()
    val sideEffect: MutableSharedFlow<DummySideEffect> = _sideEffect

    fun getDummyList() = viewModelScope.launch {
        dummyRepository.getDummyList()
            .onSuccess {
                _state.value = _state.value.copy(
                    uiState = UiState.Success(it.toPersistentList())
                )
            }.onFailure { throwable ->
                val errorMessage = handleError(throwable)
                _state.value = _state.value.copy(
                    uiState = UiState.Failure(errorMessage)
                )
                showSnackBar(errorMessage)
            }
    }

    private fun showSnackBar(message: String) = viewModelScope.launch {
        _sideEffect.emit(DummySideEffect.ShowSnackBar(message))
    }

    fun navigateUp() = viewModelScope.launch {
        _sideEffect.emit(DummySideEffect.NavigateUp)
    }

    fun navigateNext() = viewModelScope.launch {
        _sideEffect.emit(DummySideEffect.NavigateNext)
    }
}
