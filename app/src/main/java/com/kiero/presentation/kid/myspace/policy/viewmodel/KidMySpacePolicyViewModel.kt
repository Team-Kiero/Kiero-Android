package com.kiero.presentation.kid.myspace.policy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiero.data.kid.mypage.repository.KidMyPageRepository
import com.kiero.presentation.kid.myspace.policy.model.toUiModel
import com.kiero.presentation.kid.myspace.policy.state.KidMySpacePolicyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KidMySpacePolicyViewModel @Inject constructor(
    private val kidMyPageRepository: KidMyPageRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(KidMySpacePolicyState())
    val state = _state.asStateFlow()

    init {
        fetchMyPageTerms()
    }

    fun fetchMyPageTerms() {
        viewModelScope.launch {
            kidMyPageRepository.getKidTermsExternalLink()
                .onSuccess { result ->
                    _state.update { currentState ->
                        currentState.copy(
                            myPageMenus = result.map { it.toUiModel() }.toImmutableList()
                        )
                    }
                }
                .onFailure {
                    Timber.e(it, "약관 불러오기 실패")
                }
        }
    }
}