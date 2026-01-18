package com.kiero.presentation.parent.schedule.mission

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.presentation.parent.schedule.mission.component.model.MissionAwardDefaults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentAddMissionViewModel @Inject constructor() : ViewModel() {

    val missionNameState = TextFieldState()
    val awardTextFieldState = TextFieldState()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _currentAwardValue = MutableStateFlow(MissionAwardDefaults.DEFAULT_AWARD)
    val currentAwardValue: StateFlow<Int> = _currentAwardValue.asStateFlow()

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate.asStateFlow()

    init {
        observeAwardTextFieldChanges()
        observeCurrentAwardValueChanges()
    }

    @OptIn(FlowPreview::class)
    private fun observeAwardTextFieldChanges() {
        viewModelScope.launch {
            snapshotFlow { awardTextFieldState.text.toString() }
                .debounce(500)
                .collect { text ->
                    if (text.isNotEmpty()) {
                        val parsed = text.toIntOrNull() ?: MissionAwardDefaults.DEFAULT_AWARD
                        val newValue = MissionAwardDefaults.constrainValue(parsed)
                        if (newValue != _currentAwardValue.value) {
                            _currentAwardValue.value = newValue
                            if (parsed != newValue) {
                                awardTextFieldState.setTextAndPlaceCursorAtEnd(newValue.toString())
                            }
                        }
                    }
                }
        }
    }

    private fun observeCurrentAwardValueChanges() {
        viewModelScope.launch {
            _currentAwardValue.collect { value ->
                if (awardTextFieldState.text.toString().toIntOrNull() != value) {
                    awardTextFieldState.setTextAndPlaceCursorAtEnd(value.toString())
                }
            }
        }
    }

    fun onDateClick() {
        _showBottomSheet.value = true
    }

    fun onDismissBottomSheet() {
        _showBottomSheet.value = false
    }

    fun onAwardClick(change: Int) {
        _currentAwardValue.value =
            MissionAwardDefaults.applyChange(_currentAwardValue.value, change)
    }

    fun onDateSelected(date: String) {
        _selectedDate.value = date
        _showBottomSheet.value = false
    }
}