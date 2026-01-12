package com.kiero.presentation.kid.wish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.presentation.kid.wish.state.KidWishState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KidWishViewModel @Inject constructor(
) : ViewModel() {
    private val _state = MutableStateFlow(KidWishState())
    val state = _state.asStateFlow()


    fun onMissionCompleted(
        missionId: Int
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    kidMissionSectionList = it.kidMissionSectionList.map { section ->
                        section.copy(
                            missions = section.missions.map { mission ->
                                if (mission.id == missionId) {
                                    mission.copy(isCompleted = true)
                                } else {
                                    mission
                                }
                            }.toPersistentList()
                        )
                    }.toImmutableList()
                )
            }
        }
    }
}