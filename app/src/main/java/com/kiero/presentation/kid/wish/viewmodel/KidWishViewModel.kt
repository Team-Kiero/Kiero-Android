package com.kiero.presentation.kid.wish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.kid.coin.repository.CoinRepository
import com.kiero.data.mission.repository.MissionRepository
import com.kiero.presentation.kid.wish.model.toState
import com.kiero.presentation.kid.wish.state.KidWishState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidWishViewModel @Inject constructor(
    private val repository: CoinRepository,
    private val missionRepository: MissionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(KidWishState())

    // Todo: 추후 usecase 고려
    val state: StateFlow<KidWishState> = combine(
        _state,
        repository.myCoin
    ) { state, coinData ->
        KidWishState(
            kidWishList = state.kidWishList,
            coinUiModel = coinData.toState()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = KidWishState()
    )

    init {
        fetchCoin()
    }

    fun fetchCoin() {
        viewModelScope.launch {
            repository.getCurrentCoin()
                .onSuccess {
                    Timber.d("fetchCoin: $it")
                }
                .onFailure {
                    Timber.e("fetchCoin fail: $it")
                }
        }
    }
}