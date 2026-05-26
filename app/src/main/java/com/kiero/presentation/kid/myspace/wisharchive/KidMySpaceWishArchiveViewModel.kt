package com.kiero.presentation.kid.myspace.wisharchive

import androidx.lifecycle.ViewModel
import com.kiero.presentation.kid.myspace.wisharchive.state.KidMySpaceWishArchiveState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class KidMySpaceWishArchiveViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(KidMySpaceWishArchiveState.FAKE)
    val uiState: StateFlow<KidMySpaceWishArchiveState> = _uiState.asStateFlow()
}