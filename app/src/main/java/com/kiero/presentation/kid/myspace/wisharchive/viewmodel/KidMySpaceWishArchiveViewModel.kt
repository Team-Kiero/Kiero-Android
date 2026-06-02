package com.kiero.presentation.kid.myspace.wisharchive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.core.model.UiState
import com.kiero.data.kid.wish.repository.WishRepository
import com.kiero.presentation.kid.myspace.state.toUiModel
import com.kiero.presentation.kid.myspace.wisharchive.model.KidMySpaceWishArchiveItemUiModel
import com.kiero.presentation.kid.myspace.wisharchive.state.KidMySpaceWishArchiveState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class KidMySpaceWishArchiveViewModel @Inject constructor(
    private val wishRepository: WishRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<KidMySpaceWishArchiveState>>(UiState.Loading)
    val state = _state.asStateFlow()

    init {
        fetchWishArchive()
    }

    private fun fetchWishArchive() {
        viewModelScope.launch {
            wishRepository.getWishHistory()
                .onSuccess { historyList ->
                    val todayDateString = LocalDate.now().toString()

                    val todayWishes = mutableListOf<KidMySpaceWishArchiveItemUiModel>()
                    val previousWishes = mutableListOf<KidMySpaceWishArchiveItemUiModel>()

                    historyList.forEachIndexed { index, model ->
                        val uiModel = model.toUiModel(index)
                        if (model.purchasedAt == todayDateString) {
                            todayWishes.add(uiModel)
                        } else {
                            previousWishes.add(uiModel)
                        }
                    }

                    _state.value = UiState.Success(
                        KidMySpaceWishArchiveState(
                            totalCount = historyList.size,
                            todayWishes = todayWishes.toPersistentList(),
                            previousWishes = previousWishes.toPersistentList()
                        )
                    )
                }
                .onFailure { error ->
                    Timber.e("fetchWishArchive failed: $error")
                    _state.value = UiState.Failure(error.message ?: "알 수 없는 에러가 발생했습니다.")
                }
        }
    }
}