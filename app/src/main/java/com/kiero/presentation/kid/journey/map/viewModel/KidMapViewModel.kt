package com.kiero.presentation.kid.journey.map.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.kiero.presentation.kid.journey.map.navigation.Map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KidMapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val map = savedStateHandle.toRoute<Map>()

    val date: String = map.date
}