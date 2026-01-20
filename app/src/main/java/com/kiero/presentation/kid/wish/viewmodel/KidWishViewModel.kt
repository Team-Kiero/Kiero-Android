package com.kiero.presentation.kid.wish.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.common.extension.updateSuccess
import com.kiero.core.common.util.successData
import com.kiero.core.model.UiState
import com.kiero.data.kid.coin.repository.CoinRepository
import com.kiero.data.kid.wish.repository.WishRepository
import com.kiero.presentation.kid.model.toUiModel
import com.kiero.presentation.kid.wish.model.toUiModel
import com.kiero.presentation.kid.wish.state.KidWishSideEffect
import com.kiero.presentation.kid.wish.state.KidWishState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidWishViewModel @Inject constructor(
    private val repository: CoinRepository,
    private val wishRepository: WishRepository
) : ViewModel() {
    private val _state = MutableStateFlow<UiState<KidWishState>>(UiState.Loading)
    val state: StateFlow<UiState<KidWishState>> = combine(
        _state,
        repository.myCoin
    ) { uiState, coinData ->
        when (uiState) {
            is UiState.Success -> {
                UiState.Success(
                    uiState.data.copy(
                        coinUiModel = coinData.toUiModel()
                    )
                )
            }

            is UiState.Loading -> UiState.Loading
            is UiState.Failure -> UiState.Failure(uiState.message)
            is UiState.Empty -> UiState.Empty
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )

    private val _sideEffect = MutableSharedFlow<KidWishSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        fetchWish()
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

    fun fetchWish(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _state.updateSuccess { it.copy(isRefreshing = true) }
            }

            val minLoadingTime = launch {
                if (isRefresh) delay(1000)
            }

            wishRepository.getCoupons()
                .onSuccess { result ->
                    minLoadingTime.join()

                    _state.value = UiState.Success(
                        data = KidWishState(
                            kidWishList = result.map { wish ->
                                wish.toUiModel()
                            }.toImmutableList(),
                            isRefreshing = false
                        )
                    )
                }
                .onFailure {
                    minLoadingTime.join()
                    Timber.e("fetchWish fail: $it")
                    _state.updateSuccess { state -> state.copy(isRefreshing = false) }
                    _sideEffect.emit(KidWishSideEffect.ShowSnackBar(it.message.toString()))
                }
        }
    }

    fun prayWish(
        couponId: Long
    ) {
        viewModelScope.launch {
            wishRepository.patchCoupon(couponId)
                .onSuccess {
                    _state.updateSuccess {
                        it.copy(
                            isVisibleDialog = false,
                            isCompletedWish = true
                        )
                    }

                    fetchCoin()
                }
                .onFailure {
                    _sideEffect.emit(KidWishSideEffect.ShowSnackBar(it.message.toString()))
                }
        }
    }

    fun openDialogWithItem(targetId: Long) {
        val currentState = _state.value.successData ?: return

        val selectedItem = currentState.kidWishList.find { it.couponId == targetId } ?: return

        val myCoin = currentState.coinUiModel.coinAmount
        val itemPrice = selectedItem.price

        if (myCoin >= itemPrice) {
            _state.updateSuccess { state ->
                state.copy(
                    isVisibleDialog = true,
                    selectedWishItem = selectedItem
                )
            }
        } else {
            viewModelScope.launch {
                _sideEffect.emit(KidWishSideEffect.ShowSnackBar("금화가 부족해! 미션을 더 하고 오자!"))
            }
        }
    }

    fun dismissDialog() {
        _state.updateSuccess { state ->
            state.copy(
                isVisibleDialog = false,
                isCompletedWish = false
            )
        }
    }

}